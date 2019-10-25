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
		try
		{
			BufferedReader inputString = new BufferedReader(new InputStreamReader(server_.getInputStream()));
	
			// Wait for a result then process the offer.
			String s = inputString.readLine();
			String[] splitted = s.split(" ");
			
			int stocks = Integer.parseInt(splitted[2]);
			int price = Integer.parseInt(splitted[3]);
			Offer o = new Offer(splitted[1], stocks, price);
			
			if (splitted[0].equals("YOUBOUGHT"))
			{
				boolean bool = client_.bought(o);
				if(!bool)
				{
					FilePrinter.printLine("[ClientThread] " + "Error for " + client_.getClientName() + " buying from " + o.getClientName());
					System.out.println("[ClientThread] " + "Error for " + client_.getClientName() + " buying from " + o.getClientName());
				}
			}
			else
			{
				boolean bool = client_.sold(o);
				if(!bool)
				{
					FilePrinter.printLine("[ClientThread] " + "Error for " + client_.getClientName() + " Something wrong with the offer");
					System.out.println("[ClientThread] " + "Error for " + client_.getClientName() + " Something wrong with the offer");
				}
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
}

