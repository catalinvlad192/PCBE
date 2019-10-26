package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client implements Runnable
{
	
	private int numberOfStocks_;
	private int money_;
	private String clientName_;
	private Socket socket_;
	private boolean isJustObserver_;
	private Thread sellThread_, buyThread_;
	private boolean buyCompleted_;
	private boolean sellCompleted_;
	private int howManyToBuy_;
	
	public Client(String clientName, int howManyToBuy, boolean isJustObserver)
	{
		howManyToBuy_ = howManyToBuy;
		numberOfStocks_ = 10;
		money_ = 50000;
		clientName_ = clientName;
		isJustObserver_ = isJustObserver;
		buyCompleted_ = false;
		sellCompleted_ = false;
	}

	public synchronized void setSellCompleted(boolean b)
	{
		sellCompleted_ = b;
	}

	public synchronized void setBuyCompleted(boolean b)
	{
		buyCompleted_ = b;
	}
	
	public synchronized boolean getSellCompleted()
	{
		return sellCompleted_;
	}

	public synchronized boolean getBuyCompleted()
	{
		return buyCompleted_;
	}

	public String getClientName()
	{
		return clientName_;
	}
	
	public boolean sold(Offer myOffer)
	{
		if (clientName_.equals(myOffer.getClientName()))
		{
			int stocksSold = myOffer.getNumberOfStocks();
			int price = myOffer.getPricePerStock();
			synchronized(this)
			{
				numberOfStocks_ = numberOfStocks_ - stocksSold;
				money_ = money_ + stocksSold*price;
				setSellCompleted(true);
				FilePrinter.printLine("[Client] " + clientName_ +" SOLD. Current fortune is: "
						+ numberOfStocks_ + " " + money_);
				System.out.println("[Client] "+ clientName_ +" SOLD. Current fortune is: "
						+ numberOfStocks_ + " " + money_);
			}
			return true;
		}
		return false;
	}
	
	public boolean bought(Offer offer)
	{
		int stocksBought = offer.getNumberOfStocks();
		int price = offer.getPricePerStock();
		if (!clientName_.equals(offer.getClientName()))
		{
			synchronized(this)
			{
			numberOfStocks_ = numberOfStocks_ + stocksBought;
			money_ = money_ - stocksBought*price;
			setBuyCompleted(true);
			FilePrinter.printLine("[Client] " + clientName_ + " Bought from " + offer.getClientName()
					+ " Current fortune is: " + numberOfStocks_ + " " + money_);
			System.out.println("[Client] " + clientName_ + " Bought from " + offer.getClientName()
					+ " Current fortune is: " + numberOfStocks_ + " " + money_);
			}
			return true;
		}
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
	public void run()
	{
		try
		{
			socket_ = new Socket("127.0.0.1", 15432);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		
		// If observer, just reads transactions and transaction history
		if(isJustObserver_)
		{
			while (true)
			{
				try
				{
					PrintWriter outputString = new PrintWriter(socket_.getOutputStream(), true);
					BufferedReader inputString = new BufferedReader(new InputStreamReader(socket_.getInputStream()));
					
					outputString.println("START");
					outputString.println("getTransactionHistory");
					String s;
					FilePrinter.printLine("#######Here is the transaction history");
					System.out.println("#######Here is the transaction history");
					while( !(s = inputString.readLine()).equals("END") )
					{
						FilePrinter.printLine(s);
						System.out.println(s);
					}
					
					outputString.println("START");
					outputString.println("getTransactions");
					FilePrinter.printLine("#######Here are all current transactions");
					System.out.println("#######Here are all current transactions");
					while( !(s = inputString.readLine()).equals("END") )
					{
						FilePrinter.printLine(s);
						System.out.println(s);
					}
				}
				catch(IOException e)
				{
					e.printStackTrace();
				}
			}
		}
		// If normal client, send a request and an offer, in this order 
		//then wait for it to be completed before sending two more
		else
		{
			while (true)
			{
				setBuyCompleted(false);
				setSellCompleted(false);
				try
				{
					PrintWriter outputString = new PrintWriter(socket_.getOutputStream(), true);
					String s;
					
					outputString.println("START");
					
					// Send request then start a new thread
					Request req = buy(howManyToBuy_, howManyToBuy_);
					s = "BUY " + req.getClientName() + " " + req.getNumberOfStocks() + " " + req.getPricePerStock();
					outputString.println(s);
					FilePrinter.printLine("[Client] " + clientName_ + " Wants to " + s);
					System.out.println("[Client] " + clientName_ + " Wants to " + s);
					
					buyThread_ = new Thread(new ClientThread(this, socket_));
					buyThread_.start();

					// Send offer then start a new thread
					Offer off = sell(howManyToBuy_, howManyToBuy_);
					s = "SELL " + off.getClientName() + " " + off.getNumberOfStocks() + " " + off.getPricePerStock();
					outputString.println(s);
					FilePrinter.printLine("[Client] " + clientName_ + "Wants to " + s);
					System.out.println("[Client] " + clientName_ + "Wants to " + s);
					
					sellThread_ = new Thread(new ClientThread(this, socket_));
					sellThread_.start();
					
					while(true)
					{
						if(getBuyCompleted() && getSellCompleted())
							break;
					}
					FilePrinter.printLine("[Client] " + clientName_ + " The two transactions were completed!");
					System.out.println("[Client] " + clientName_ + " The two transactions were completed!");
				}
				catch(IOException e)
				{
					e.printStackTrace();
				}
			}
		}
	}
}
