package main;

import java.io.BufferedReader;
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
		System.out.println("[ServerThread]Server communicates with Client and ClientThread");
		try
		{
			PrintWriter outputString = new PrintWriter(client_.getOutputStream(), true);
			BufferedReader inputString = new BufferedReader(new InputStreamReader(client_.getInputStream()));
			// receive request or offer and send back message.
			
			String read = inputString.readLine();
			String[] splittedRead = read.split(" ");
			System.out.println("[ServerThread] Received transaction. Processing... ");
			System.out.println("[ServerThread] " + splittedRead[0] + " " + splittedRead[1] + " " + splittedRead[2] + " " + splittedRead[3]);
			
			
			if(splittedRead[0].equals("BUY"))
			{
				System.out.println("[ServerThread] Processing Request...");
				int stocks = Integer.parseInt(splittedRead[2]);
				int price = Integer.parseInt(splittedRead[3]);
				Request r = new Request(splittedRead[1], stocks, price);
				server_.addTransaction(r);
				
				while(true)
				{
					System.out.println("[ServerThread] Waiting for request completion...");
					Offer o;
					if( ( o = server_.find(r)) !=null )
					{
						System.out.println("[ServerThread] Request Completed...");
						String write = o.getClientName() + " " + o.getNumberOfStocks() + " " + o.getPricePerStock();
						outputString.println(write);
						break;
					}
				}
			}
			else
			{
				System.out.println("[ServerThread] Processing Offer...");
				int stocks = Integer.parseInt(splittedRead[2]);
				int price = Integer.parseInt(splittedRead[3]);
				Offer off = new Offer(splittedRead[1], stocks, price);
				server_.addTransaction(off);
				
				while(true)
				{
					System.out.println("[ServerThread] Waiting for offer completion...");
					if( (server_.find(off.getClientName())) == null)
					{
						System.out.println("[ServerThread] Offer Completed...");
						String write = off.getClientName() + " " + off.getNumberOfStocks() + " " + off.getPricePerStock();
						outputString.println(write);
						break;
					}
				}
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
				client_.close();
			} catch(IOException e)
			{
				e.printStackTrace();
			}
		}
	}
}

