package transaction;

public class Offer implements ITransaction
{
	private String clientName_;
	private int numberOfStocks_;
	private int pricePerStock_;
	
	public Offer(String clientName, int numberOfStocks, int pricePerStock)
	{
		clientName_ = clientName;
		numberOfStocks_ = numberOfStocks;
		pricePerStock_ = pricePerStock;
	}
	
	public void modify(int numberOfStocks, int price)
	{
		numberOfStocks_ = numberOfStocks;
		pricePerStock_ = price;
	}
	
	public String getClientName()
	{
		return clientName_;
	}
	public int getStocks()
	{
		return numberOfStocks_;
	}
	public int getPrice()
	{
		return pricePerStock_;
	}
	public boolean isForSale()
	{
		return true;
	}
}
