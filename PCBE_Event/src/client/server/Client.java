package client.server;

import transaction.ITransaction;
import transaction.Offer;
import transaction.Request;

import java.util.ArrayList;

import main.FilePrinter;
import observer.pattern.IObserver;
import observer.pattern.Notification;

public class Client extends IObserver
{
	private String clientName_;
	private int stocks_;
	private int stocksWanted_;
	private int money_;
	private Server server_;
	
	private boolean isBuying_;
	private boolean isSelling_;
	private boolean isWatcher_;
	
	private boolean isOfferSent_;

	public Client(Server s, String name, int stocksWanted, boolean isBuying, boolean isSelling, boolean isWatcher)
	{
		super(name);
		server_ = s;
		clientName_ = name;
		stocks_ = 10;
		stocksWanted_ = stocksWanted;
		money_ = 10000;
		
		isBuying_ = isBuying;
		isSelling_ = isSelling;
		isWatcher_ = isWatcher;
		
		isOfferSent_ = false;
	}

	// Sold (remove offer from server)
	public boolean sold(Offer myOffer)
	{
		System.out.println("[Client] Client " + clientName_ + " TRYING TO SELL. Initial values" + stocks_ + " " + money_ + " StocksReceived: " + myOffer.getStocks() + " StocksWanted: " + stocksWanted_ + " PriceReceived: " + myOffer.getPrice());
		FilePrinter.printLine("[Client] Client " + clientName_ + " TRYING TO SELL. Initial values" + stocks_ + " " + money_ + " StocksReceived: " + myOffer.getStocks() + " StocksWanted: " + stocksWanted_ + " PriceReceived: " + myOffer.getPrice());
		if (clientName_.equals(myOffer.getClientName()) && myOffer.getStocks() == stocksWanted_)
		{
			int stocksSold = myOffer.getStocks();
			int price = myOffer.getPrice();

			stocks_ = stocks_ - stocksSold;
			money_ = money_ + stocksSold*price;
			System.out.println("[Client] Client " + clientName_ + " SOLD. Values afte selling" + stocks_ + " " + money_);
			FilePrinter.printLine("[Client] Client " + clientName_ + " SOLD. Values afte selling" + stocks_ + " " + money_);
			return true;
		}
		return false;
	}

	// Bought (notification)
	public boolean bought(Offer offer)
	{
		System.out.println("[Client] Client " + clientName_ + " TRYING TO BUY. Initial values" + stocks_ + " " + money_ + " StocksReceived: " + offer.getStocks() + " StocksWanted: " + stocksWanted_ + " PriceReceived: " + offer.getPrice());
		FilePrinter.printLine("[Client] Client " + clientName_ + " TRYING TO BUY. Initial values" + stocks_ + " " + money_ + " StocksReceived: " + offer.getStocks() + " StocksWanted: " + stocksWanted_ + " PriceReceived: " + offer.getPrice());
		int stocksBought = offer.getStocks();
		int price = offer.getPrice();
		if (!clientName_.equals(offer.getClientName()) && stocksBought == stocksWanted_)
		{
			stocks_ = stocks_ + stocksBought;
			money_ = money_ - stocksBought*price;
			
			System.out.println("[Client] Client " + clientName_ + " BOUGHT. Values afte purchase" + stocks_ + " " + money_);
			FilePrinter.printLine("[Client] Client " + clientName_ + " BOUGHT. Values afte purchase" + stocks_ + " " + money_);
			
			return true;
		}
		return false;
	}
	
	public void run()
	{
		if(isWatcher_)
		{
			ArrayList<String> x = server_.getTransactionHistory();
			
			System.out.println("[Client] Here is the transaction history#############");
			FilePrinter.printLine("[Client] Here is the transaction history#############");
			for(String s : x)
			{
				System.out.println(s);
				FilePrinter.printLine(s);
			}
			
			int xrand = (int) (Math.random()*100)%6 + 1;
			String s = "Client" + xrand;
			ITransaction tr = server_.getTransaction(s);
			if(tr != null) {
				System.out.println("[Client] Reading transaction from " + tr.getClientName() + " " + tr.getStocks() +  " " + tr.getPrice());
				FilePrinter.printLine("[Client] Reading transaction from " + tr.getClientName() + " " + tr.getStocks() +  " " + tr.getPrice());
			}
		}
		
		if(isBuying_)
		{
			if(isOfferSent_ == false)
			{
				server_.addTransaction(new Request(clientName_, stocksWanted_, stocksWanted_));
				isOfferSent_ = true;
			}
			else
			{
				server_.find(new Request(clientName_, stocksWanted_, stocksWanted_));
			}
		}
		
		if(isSelling_)
		{
			if(isOfferSent_ == false)
			{
				server_.addTransaction(new Offer(clientName_, stocksWanted_, stocksWanted_));
				isOfferSent_ = true;
			}
			else
			{
				// waiting
			}
		}
	}

	@Override
	public void update(Notification notification)
	{
		if(isWatcher_)
		{
			System.out.println("[Client] Client " + clientName_ + " is watching " + notification.getTransaction().getClientName());
			FilePrinter.printLine("[Client] Client " + clientName_ + " is watching " + notification.getTransaction().getClientName());
		}
		else
		{
			if(notification.wasRead() && notification.getTransaction().getClientName().equals(clientName_))
			{
				System.out.println("[Client] Client " + clientName_ + " Someone just read my offer");
				FilePrinter.printLine("[Client] Client " + clientName_ + " has bought from " + notification.getTransaction().getClientName());
			}
		}
		if(isBuying_)
		{
			if(notification.getTransaction().isForSale() && bought((Offer) notification.getTransaction()))
			{
				System.out.println("[Client] Client " + clientName_ + " has bought from " + notification.getTransaction().getClientName());
				FilePrinter.printLine("[Client] Client " + clientName_ + " has bought from " + notification.getTransaction().getClientName());
				isOfferSent_ = false;
				isBuying_ = false;
				isSelling_ = true;
			}
		}
		if(isSelling_)
		{
			if(notification.getTransaction().isForSale() && sold((Offer) notification.getTransaction()))
			{
				System.out.println("[Client] Client " + clientName_ + " has sold");
				FilePrinter.printLine("[Client] Client " + clientName_ + " has sold");
				isOfferSent_ = false;
				isBuying_ = true;
				isSelling_ = false;
			}
		}
	}
}
