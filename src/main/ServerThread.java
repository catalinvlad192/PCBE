package main;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerThread extends Thread
{
	private Socket client_;
	private Server server_;
	
	public ServerThread(Server server, Socket client)
	{
		server_ = server;
		client_ = client;
	}
	
	public void run()
	{
		System.out.println("[ServerThread]Server communicates with client");
		try
		{
			PrintWriter outputString = new PrintWriter(client_.getOutputStream(), true);
			BufferedReader inputString = new BufferedReader(new InputStreamReader(client_.getInputStream()));
			DataOutputStream outputData = new DataOutputStream(client_.getOutputStream());
			DataInputStream inputData = new DataInputStream(client_.getInputStream());
			// receive request or offer and send back message.
			
			String s = inputString.readLine();
			s = s.substring(0, s.length());
			System.out.println("[ServerThread] Read s" + s);
			if(s.equals("REQ"))
			{
				System.out.println("[ServerThread] REQ");
				String client = inputString.readLine();
				client = client.substring(0, client.length());
				System.out.println("[ServerThread] Read Client" + client);
				int stocks = inputData.readInt();
				System.out.println("[ServerThread] Read stocks" + stocks);
				int price = inputData.readInt();
				System.out.println("[ServerThread] Read price" + price);
				Request r = new Request(client, stocks, price);
				server_.addTransaction(r);
				
				while(true)
				{
					Offer o = server_.find(r);
					if (o != null)
					{
						// Send Offer to buyer
						outputString.println(o.getClientName());
						outputData.writeInt(o.getNumberOfStocks());
						outputData.writeInt(o.getPricePerStock());
						break;
					}
					Thread.sleep(100);
				}
			}
			if(s.equals("OFF"))
			{
				System.out.println("[ServerThread] REQ");
				String client = inputString.readLine();
				client = client.substring(0, client.length());
				System.out.println("[ServerThread] Read Client" + client);
				int stocks = inputData.readInt();
				System.out.println("[ServerThread] Read stocks" + stocks);
				int price = inputData.readInt();
				Offer r = new Offer(client, stocks, price);
				System.out.println("[ServerThread] Read price" + price);
				server_.addTransaction(r);
				
				while(true)
				{
					Offer o = server_.find(client);
					if (o == null)
					{
						// Send Offer back to seller
						outputString.println("Success!");
						break;
					}
					Thread.sleep(100);
				}
			}
		}
		catch(InterruptedException a)
		{
			a.printStackTrace();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				client_.close();
			} catch(IOException e)
			{
				e.printStackTrace();
			}
		}
	}
}

