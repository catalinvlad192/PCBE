package main;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client implements Runnable{
	
	private int numberOfStocks_;
	private int money_;
	private String clientName_;
	private Socket socket_;
	
	public Client(String clientName, int numberOfStocks)
	{
		numberOfStocks_ = numberOfStocks;
		money_ = 10000;
		clientName_ = clientName;
	}
	
	public String getClientName()
	{
		return clientName_;
	}
	
	private Offer sell(int numberOfStocks, int pricePerStock)
	{
		return new Offer(clientName_, numberOfStocks, pricePerStock);
	}
	
	private boolean sold(Offer myOffer)
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
	
	private Request buy(int numberOfStocks, int pricePerStock)
	{
		return new Request(clientName_, numberOfStocks, pricePerStock);
	}
	
	private boolean bought(Offer offer)
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
				
				PrintWriter outputString = new PrintWriter(socket_.getOutputStream(), true);
				BufferedReader inputString = new BufferedReader(new InputStreamReader(socket_.getInputStream()));
				DataOutputStream outputData = new DataOutputStream(socket_.getOutputStream());
				DataInputStream inputData = new DataInputStream(socket_.getInputStream());
				
				double random = Math.random();
				boolean isBuying;
				
				// Try to buy if you have money
				if(random < 0.5 && money_ > 0)
				{
					// send request
					isBuying = true;
					Request req = buy(10, 1000);
					outputString.println("REQ");
					System.out.println("[Client] write REQ");
					outputString.println(req.getClientName());
					System.out.println("[Client] write client name " + req.getClientName());
					outputData.writeInt(req.getNumberOfStocks());
					System.out.println("[Client] write client stocks " + req.getNumberOfStocks());
					outputData.writeInt(req.getPricePerStock());
					System.out.println("[Client] write client price " + req.getPricePerStock());
					System.out.println("[Client]" + clientName_ + " Wants to buy");
					
				} else // Try to sell otherwise
				{
					// send offer;
					isBuying = false;
					Offer off = sell(10, 1000);
					outputString.println("OFF");
					System.out.println("[Client] write OFF");
					outputString.println(off.getClientName());
					System.out.println("[Client] write client name " + off.getClientName());
					outputData.writeInt(off.getNumberOfStocks());
					System.out.println("[Client] write client stocks " + off.getNumberOfStocks());
					outputData.writeInt(off.getPricePerStock());
					System.out.println("[Client] write client price " + off.getPricePerStock());
					System.out.println("[Client]" + clientName_ + " Wants to sell");
				}
				
				// If you are buying, just wait for the seller
				if(isBuying)
				{
					String client = inputString.readLine();
					client = client.substring(0, client.length());
					int stocksInt = inputData.readInt();
					int priceInt = inputData.readInt();
					
					Offer serverOffer = new Offer(client, stocksInt, priceInt);
					bought(serverOffer);
					System.out.println("[Client]" + clientName_ + " bought from " + serverOffer.getClientName());
				}
				else // Check from time to time if someone bought your stocks
				{
					String s = inputString.readLine();
					sold(sell(10, 1000));
				}
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
		}
	}
}
