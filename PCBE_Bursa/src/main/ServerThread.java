package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ServerThread extends Thread
{
	private Socket client_;
	private Server server_;
	private Thread serverTransactionThread_;
	
	public ServerThread(Server server, Socket client)
	{
		server_ = server;
		client_ = client;
	}
	
	public void run()
	{
		try
		{
			BufferedReader inputString = new BufferedReader(new InputStreamReader(client_.getInputStream()));
			PrintWriter outputString = new PrintWriter(client_.getOutputStream(), true);
			
			while (true)
			{
				String read = inputString.readLine();
				if(read!=null && read.equals("START"))
				{
					read = inputString.readLine();
					
					// if client wants to read transaction history, send it to him
					if(read.equals("getTransactionHistory") )
					{
						ArrayList<String> history = server_.getTransactionHistory();
						
						for(String s : history)
						{
							outputString.println(s);
						}
						outputString.println("END");
					}
					// if client wants to read all current transactions, send them to him
					else if(read.equals("getTransactions") )
					{
						ArrayList<String> transactions = server_.getTransactions();
						
						for(String s : transactions)
						{
							outputString.println(s);
						}
						outputString.println("END");
					}
					// Else read the request and offer sent by the client, then start a thread
					// for each to find matches for them
					else
					{
						String[] splittedRead = read.split(" ");
						int stocks = Integer.parseInt(splittedRead[2]);
						int price = Integer.parseInt(splittedRead[3]);
						
						Request req = new Request(splittedRead[1], stocks, price);
						server_.addTransaction(req);
						serverTransactionThread_ = new Thread(new ServerTransactionThread(server_, client_, req));
						serverTransactionThread_.start();

						read = inputString.readLine();
						splittedRead = read.split(" ");
						stocks = Integer.parseInt(splittedRead[2]);
						price = Integer.parseInt(splittedRead[3]);
						
						Offer off = new Offer(splittedRead[1], stocks, price);
						server_.addTransaction(off);
						serverTransactionThread_ = new Thread(new ServerTransactionThread(server_, client_, off));
						serverTransactionThread_.start();
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
				System.out.println("#$#$#$$#$#$#$#$#$#$#$#$#$CLOSED SOCKET");
				client_.close();
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
		}
	}
}

