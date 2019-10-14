package main;

public interface ITransaction {
	public String getClientName();
	public int getNumberOfStocks();
	public int getPricePerStock();
	public boolean isForSale();
}