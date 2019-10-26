package main;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Server implements Runnable
{
	
	private HashMap<String, ITransaction> transactions_;
	private ServerSocket serverSocket_;
	private ArrayList<Pair<String, String>> transactionHistory_;
	private ArrayList<Thread> listOfThreads_ = new ArrayList<Thread>();
	
	public Server()
	{
		transactions_ = new HashMap<String, ITransaction>();
		transactionHistory_ = new ArrayList<Pair<String, String>>();
	}
	
	public synchronized ArrayList<String> getTransactionHistory()
	{
		ArrayList<String> copy = new ArrayList<String>();
		
		for(Pair<String, String> entry : transactionHistory_)
		{
			String s = entry.first + " bought from " + entry.second;
			copy.add(s);
		}
		return copy;
	}
	
	public synchronized ArrayList<String> getTransactions()
	{
		ArrayList<String> copy = new ArrayList<String>();
				
		for (Map.Entry<String,ITransaction> entry : transactions_.entrySet())
		{
			if(entry.getValue().isForSale())
			{
				String s = "OFFER " + entry.getValue().getClientName() + " " + entry.getValue().getNumberOfStocks()
						+ " " + entry.getValue().getPricePerStock();
				copy.add(s);
			}
			else
			{
				String s = "REQUEST " + entry.getValue().getClientName() + " " + entry.getValue().getNumberOfStocks()
						+ " " + entry.getValue().getPricePerStock();
				copy.add(s);
			}
		}
		return copy;
	}
	
	public synchronized void addTransaction(ITransaction t)
	{
		if(t.isForSale())
		{
			transactions_.put(t.getClientName() + "OFF", t);
		}
		else
		{
			transactions_.put(t.getClientName() + "REQ", t);
		}
		FilePrinter.printLine("[Server] Add transaction for " + t.getClientName()
				+t.getNumberOfStocks() + t.getPricePerStock()+ " SIZE: " + transactions_.size());
		System.out.println("[Server] Add transaction for " + t.getClientName()
				+t.getNumberOfStocks() + t.getPricePerStock()+ " SIZE: " + transactions_.size());
	}
	
	// Find a suitable offer for this request, then delete both from transactions map
	public Offer find(Request r)
	{
		for(Map.Entry<String, ITransaction> tr : transactions_.entrySet())
		{
			ITransaction transaction = tr.getValue();
			if(transaction.isForSale() && 
					!(transaction.getClientName().equals(r.getClientName())) &&
					transaction.getNumberOfStocks() == r.getNumberOfStocks() &&
					transaction.getPricePerStock() == r.getPricePerStock())
			{
				Offer actualBought = null;
				actualBought = new Offer(transaction.getClientName(), transaction.getNumberOfStocks(),
						transaction.getPricePerStock());
				transactionHistory_.add(new Pair<String, String>(r.getClientName(), transaction.getClientName()));
				transactions_.remove(transaction.getClientName()+"OFF");
				transactions_.remove(r.getClientName()+"REQ");
				FilePrinter.printLine("[Server] Removed REQ" + r.getClientName() + " and OFFER"
						+ transaction.getClientName() + " SIZE: " + transactions_.size());
				System.out.println("[Server] Removed REQ" + r.getClientName() + " and OFFER"
						+ transaction.getClientName() + " SIZE: " + transactions_.size());
				return (Offer)actualBought;
			}
		}
		return null;
	}
	
	// Check if your offer was bought by someone
	public Offer find(String client)
	{
		ITransaction tr = transactions_.get(client + "OFF");
		if( tr!=null && tr.isForSale() && tr.getClientName().equals(client))
		{
			return (Offer)tr;	
		}
		return null;
	}

	@Override
	public void run() {
		try
		{
			serverSocket_ = new ServerSocket(15432);
			while(true)
			{
				Socket client = serverSocket_.accept();
				
				Thread thread = new Thread(new ServerThread(this, client));
				listOfThreads_.add(thread);
				thread.start();
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
}
