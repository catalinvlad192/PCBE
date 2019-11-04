package transaction;

public interface ITransaction
{
	public String getClientName();
	public int getStocks();
	public int getPrice();
	public boolean isForSale();
}