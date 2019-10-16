package main;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server implements Runnable{
	
	private ArrayList<ITransaction> transactions_;
	private ServerSocket serverSocket_;
	private ArrayList<Thread> listOfThreads_ = new ArrayList<Thread>();
	
	public Server()
	{
		transactions_ = new ArrayList<ITransaction>();
	}
	
	public ArrayList<ITransaction> getTransactions()
	{
		return transactions_;
	}
	
	public synchronized void addTransaction(ITransaction t)
	{
		transactions_.add(t);
	}
	
	public synchronized Offer find(Request r)
	{
		// TODO:
		return new Offer("", 1, 1);
	}
	
	public synchronized Offer find(String client)
	{
		// TODO:
		return new Offer("", 1, 1);
	}

	@Override
	public void run() {
		try
		{
			serverSocket_ = new ServerSocket(15432);
			System.out.println("[Server] Server started for port 15432");
			
			while(true)
			{
				Socket client = serverSocket_.accept();
				
				Thread thread = new Thread(new ServerThread(this, client));
				listOfThreads_.add(thread);
				thread.start();
				System.out.println("[Server] New thread started");
			}
			
		} catch(IOException e)
		{
			e.printStackTrace();
		}
	}
}
