package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client implements Runnable{
	
	private int numberOfStocks_;
	private int money_;
	private String clientName_;
	private Socket socket_;
	private PrintWriter output_;
	private BufferedReader input_;
	
	public Client(String clientName, int numberOfStocks)
	{
		numberOfStocks_ = numberOfStocks;
		money_ = 5000;
		clientName_ = clientName;
	}
	
	public Offer sell(int numberOfStocks, int pricePerStock)
	{
		return new Offer(clientName_, numberOfStocks, pricePerStock);
	}
	
	public boolean sold(Offer myOffer)
	{
		if (clientName_.equals(myOffer.getClientName()))
		{
			int stocksSold = myOffer.getNumberOfStocks();
			int price = myOffer.getPricePerStock();
			numberOfStocks_ = numberOfStocks_ - stocksSold;
			money_ = money_ + stocksSold*price;
			return true;
		}
		return false;
	}
	
	public Request buy(int numberOfStocks, int pricePerStock)
	{
		return new Request(clientName_, numberOfStocks, pricePerStock);
	}
	
	public boolean bought(Offer offer)
	{
		int stocksBought = offer.getNumberOfStocks();
		int price = offer.getPricePerStock();
		
		if (!clientName_.equals(offer.getClientName()))
		{
			numberOfStocks_ = numberOfStocks_ + stocksBought;
			money_ = money_ - stocksBought*price;
			return true;
		}
		return false;
	}

	@Override
	public void run() {
		while (true)
		{
			try {
				socket_ = new Socket("127.0.0.1", 15432);
				double random = Math.random();
				output_ = new PrintWriter(socket_.getOutputStream(), true);
				input_ = new BufferedReader(new InputStreamReader(socket_.getInputStream()));
				
				if(random < 0.5)
				{
					// send request and wait;
				} else
				{
					// send offer and wait;
				}
			}catch(IOException e)
			{
				e.printStackTrace();
			}
		}
	}
}
