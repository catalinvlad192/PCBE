package client.server;

import transaction.Offer;
import observer.pattern.IObserver;
import transaction.ITransaction;
import validators.IValidator;

public class Client extends IObserver
{
	private String clientName_;
	private int stocks_;
	private int money_;
	private Server server_;

	public Client(String name, int stocks, int money, Server s)
	{
		clientName_ = name;
		stocks_ = stocks;
		money_ = money;
		server_ = s;
	}
	
	// Sold (remove offer from server)
	public boolean sold(Offer myOffer)
	{
		if (clientName_.equals(myOffer.getClientName()))
		{
			int stocksSold = myOffer.getStocks();
			int price = myOffer.getPrice();

			stocks_ = stocks_ - stocksSold;
			money_ = money_ + stocksSold*price;
			return true;
		}
		return false;
	}
	
	// Bought (notification)
	public boolean bought(Offer offer)
	{
		int stocksBought = offer.getStocks();
		int price = offer.getPrice();
		if (!clientName_.equals(offer.getClientName()))
		{
			stocks_ = stocks_ + stocksBought;
			money_ = money_ - stocksBought*price;
			return true;
		}
		return false;
	}

	@Override
	public void update(ITransaction transaction)
	{
		// Process transaction
	}
}
