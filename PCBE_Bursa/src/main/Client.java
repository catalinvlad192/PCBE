package main;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class Client implements Runnable{
	
	private int numberOfStocks_;
	private int money_;
	private String clientName_;
	private Socket socket_;
	private boolean buy_;
	private Thread thread_;
	
	public Client(String clientName, int numberOfStocks, boolean buy)
	{
		numberOfStocks_ = numberOfStocks;
		money_ = 10000;
		clientName_ = clientName;
		buy_ = buy;
	}
	
	public String getClientName()
	{
		return clientName_;
	}
	
	public boolean isBuying()
	{
		return buy_;
	}
	
	public synchronized boolean sold(Offer myOffer)
	{
		System.out.println("[Client] Trying to sell");
		if (clientName_.equals(myOffer.getClientName()))
		{
			int stocksSold = myOffer.getNumberOfStocks();
			int price = myOffer.getPricePerStock();
			System.out.println("[Client] selling " + numberOfStocks_ + " " + money_);
			numberOfStocks_ = numberOfStocks_ - stocksSold;
			money_ = money_ + stocksSold*price;
			System.out.println("[Client] sold " + numberOfStocks_ + " " + money_);
			buy_ = !buy_;
			notifyAll();
			return true;
		}
		notifyAll();
		return false;
	}
	
	public synchronized boolean bought(Offer offer)
	{
		System.out.println("[Client] Trying to buy");
		int stocksBought = offer.getNumberOfStocks();
		int price = offer.getPricePerStock();
		System.out.println("[Client] OFFERbuy " + stocksBought + " " + price);
		
		if (!clientName_.equals(offer.getClientName()))
		{
			System.out.println("[Client] Buying " + numberOfStocks_ + " " + money_);
			numberOfStocks_ = numberOfStocks_ + stocksBought;
			money_ = money_ - stocksBought*price;
			buy_ = !buy_;
			System.out.println("[Client] Bought " + numberOfStocks_ + " " + money_);
			notifyAll();
			return true;
		}
		notifyAll();
		return false;
	}
	
	private Offer sell(int numberOfStocks, int pricePerStock)
	{
		return new Offer(clientName_, numberOfStocks, pricePerStock);
	}
	
	private Request buy(int numberOfStocks, int pricePerStock)
	{
		return new Request(clientName_, numberOfStocks, pricePerStock);
	}

	@Override
	public void run() {
		while (true)
		{
			try {
				socket_ = new Socket("127.0.0.1", 15432);
				
				PrintWriter outputString = new PrintWriter(socket_.getOutputStream(), true);
				
				if(buy_)
				{
					Request req = buy(10, 1000);
					String s = "BUY " + req.getClientName() + " " + req.getNumberOfStocks() + " " + req.getPricePerStock();
					outputString.println(s);
					System.out.println("[Client] " + s);
				}
				else
				{
					Offer off = sell(10, 1000);
					String s = "SELL " + off.getClientName() + " " + off.getNumberOfStocks() + " " + off.getPricePerStock();
					outputString.println(s);
					System.out.println("[Client] " + s);
				}
				
				boolean buyFormer = buy_;
				thread_ = new Thread(new ClientThread(this, socket_));
				thread_.start();
				System.out.println("[Client] New thread started for waiting response");
				
				while(buyFormer == buy_)
				{
					//do some actions;
				}
				//outputString.println("TR_OK");
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
		}
	}
}
