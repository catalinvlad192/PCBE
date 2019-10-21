package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientThread extends Thread
{
	private Socket server_;
	private Client client_;
	
	public ClientThread(Client client, Socket server)
	{
		server_ = server;
		client_ = client;
	}
	
	public void run()
	{
		System.out.println("[ClientThread]ServerThread communicates with ClientThread");
		try
		{
			BufferedReader inputString = new BufferedReader(new InputStreamReader(server_.getInputStream()));
			// receive request or offer and send back message.
			
			String s = inputString.readLine();
			String[] splitted = s.split(" ");
			
			System.out.println("[ClientThread] Got response from server. Processing");
			System.out.println(splitted[0] + " " + splitted[1] + " " + splitted[2]);
			
			int stocks = Integer.parseInt(splitted[1]);
			int price = Integer.parseInt(splitted[2]);
			Offer o = new Offer(splitted[0], stocks, price);
			if (client_.isBuying())
			{
				boolean bool = client_.bought(o);
				if(!bool) System.out.println("[ClientThread] " + "Error for " + client_.getClientName() + " buying from " + o.getClientName());
			}
			else
			{
				boolean bool = client_.sold(o);
				if(!bool) System.out.println("[ClientThread] " + "Error for " + client_.getClientName() + " Something wrong with the offer");
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				server_.close();
			} catch(IOException e)
			{
				e.printStackTrace();
			}
		}
	}
}

