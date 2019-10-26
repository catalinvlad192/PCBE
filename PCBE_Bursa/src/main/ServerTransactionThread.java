package main;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerTransactionThread extends Thread
{
	private Socket client_;
	private Server server_;
	private ITransaction transaction_;
	
	ServerTransactionThread(Server server, Socket client, ITransaction tr)
	{
		client_ = client;
		server_ = server;
		transaction_ = tr;
	}
	
	public void run()
	{
		try
		{
			PrintWriter outputString = new PrintWriter(client_.getOutputStream(), true);
	
			// Find suitable offer for the given request then send response to client
			if(!transaction_.isForSale())
			{
				while(true)
				{
					synchronized(server_)
					{	
						Offer o = server_.find((Request) transaction_);
						if( o !=null )
						{
							String write = "YOUBOUGHT " + o.getClientName() + " " + o.getNumberOfStocks()
									+ " " + o.getPricePerStock();
							outputString.println(write);
							break;
						}
					}
				}
			}
			// Check if given offer was already taken, then send response to client
			else
			{	
				while(true)
				{
					synchronized(server_)
					{
						Offer o = server_.find(transaction_.getClientName());
						if( o == null)
						{
							String write = "YOUSOLD " + transaction_.getClientName() + " "
									+ transaction_.getNumberOfStocks() + " " + transaction_.getPricePerStock();
							outputString.println(write);
							break;
						}
					}
				}
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}

}
