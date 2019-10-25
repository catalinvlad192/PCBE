package main;

public class Request implements ITransaction
{

	private String clientName_;
	private int numberOfStocks_;
	private int pricePerStock_;
	
	public Request(String clientName, int numberOfStocks, int pricePerStock)
	{
		clientName_ = clientName;
		numberOfStocks_ = numberOfStocks;
		pricePerStock_ = pricePerStock;
	}
	
	public String getClientName()
	{
		return clientName_;
	}
	public int getNumberOfStocks()
	{
		return numberOfStocks_;
	}
	public int getPricePerStock()
	{
		return pricePerStock_;
	}
	public boolean isForSale()
	{
		return false;
	}
}