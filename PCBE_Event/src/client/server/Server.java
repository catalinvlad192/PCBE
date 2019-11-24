package client.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import main.FilePrinter;
import transaction.Offer;
import transaction.Request;
import transaction.ITransaction;
import observer.pattern.ISubject;
import observer.pattern.Notification;

public class Server extends ISubject
{
	private HashMap<String, ITransaction> transactions_;
	private ArrayList<String> transactionHistory_;
	
	public Server()
	{
		System.out.println("[Server] Created");
		FilePrinter.printLine("[Server] Created");
		transactions_ = new HashMap<String, ITransaction>();
		transactionHistory_ = new ArrayList<String>();
	}

	public ArrayList<String> getTransactionHistory()
	{
		System.out.println("[Server] Client reading transactions");
		FilePrinter.printLine("[Server] Client reading transactions");
		return transactionHistory_;
	}

	public ITransaction getTransaction(String name)
	{
		for (ITransaction tr : transactions_.values())
		{
			if(tr.getClientName().equals(name))
			{
				System.out.println("[Server] Someone reading transaction of client: " + name);
				FilePrinter.printLine("[Server] Someone reading transaction of client: " + name);
				Notification n = new Notification(tr, false, false, true, false);
				notifyObservers(n);
				return tr;
			}
		}
		return null;
	}

	public void addTransaction(ITransaction t)
	{
		transactions_.put(t.getClientName(), t);
		System.out.println("[Server] Add transaction for " + t.getClientName());
		FilePrinter.printLine("[Server] Add transaction for " + t.getClientName());
		Notification n = new Notification(t, false, false, false, true);
		notifyObservers(n);
	}
	
	// Find a suitable offer for this request, then delete both from transactions map
	public Offer find(Request r)
	{
		for(Map.Entry<String, ITransaction> tr : transactions_.entrySet())
		{
			ITransaction transaction = tr.getValue();
			if(transaction.isForSale() && 
					!(transaction.getClientName().equals(r.getClientName())) &&
					transaction.getStocks() == r.getStocks() &&
					transaction.getPrice() == r.getPrice())
			{
				Offer actualBought = null;
				actualBought = new Offer(transaction.getClientName(), transaction.getStocks(),
						transaction.getPrice());
				transactionHistory_.add(r.getClientName() + " " + transaction.getClientName());
				transactions_.remove(transaction.getClientName());
				transactions_.remove(r.getClientName());
				System.out.println("[Server] Removed REQ " + r.getClientName() + " and OFFER "
						+ transaction.getClientName() + " SIZE: " + transactions_.size());
				FilePrinter.printLine("[Server] Removed REQ " + r.getClientName() + " and OFFER "
						+ transaction.getClientName() + " SIZE: " + transactions_.size());
				Notification n = new Notification(actualBought, true, false, false, false);
				notifyObservers(n);
				return (Offer)actualBought;
			}
		}
		return null;
	}
}
