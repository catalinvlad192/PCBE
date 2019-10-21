package main;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Server implements Runnable{
	
	private HashMap<String, ITransaction> transactions_;
	private ServerSocket serverSocket_;
	private ArrayList<Thread> listOfThreads_ = new ArrayList<Thread>();
	
	public Server()
	{
		transactions_ = new HashMap<String, ITransaction>();
	}
	
	public HashMap<String, ITransaction> getTransactions()
	{
		return transactions_;
	}
	
	public synchronized void addTransaction(ITransaction t)
	{
		System.out.println("[Server] Add transaction for " + t.getClientName());
		transactions_.put(t.getClientName(), t);
		notifyAll();
	}
	
	public synchronized Offer find(Request r)
	{
		// TODO:

		for(Map.Entry<String, ITransaction> tr : transactions_.entrySet()) {
			ITransaction transaction = tr.getValue();
			if(transaction.isForSale() && 
					!(transaction.getClientName().equals(r.getClientName())) &&
					transaction.getNumberOfStocks() >= r.getNumberOfStocks() &&
					transaction.getPricePerStock() <= r.getPricePerStock())
			{
				Offer actualBought = null;
				if(transaction.getNumberOfStocks() == r.getNumberOfStocks())
				{
					actualBought = new Offer(transaction.getClientName(), transaction.getNumberOfStocks(), transaction.getPricePerStock());
					transactions_.remove(transaction.getClientName());
				}
				notifyAll();
				return (Offer)actualBought;
			}
		}
		notifyAll();
		return null;
	}
	
	public synchronized Offer find(String client)
	{
		// TODO:
		ITransaction tr = transactions_.get(client);
		if( tr!=null && tr.isForSale() && tr.getClientName().equals(client))
		{
			notifyAll();
			return (Offer)tr;	
		}
		notifyAll();
		return null;
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
