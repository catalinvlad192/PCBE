package observer.pattern;

import transaction.ITransaction;

public class Notification
{
	private ITransaction transaction_;
	private boolean wasBought_;
	private boolean wasSold_;
	private boolean read_;
	private boolean added_;
	
	public Notification(ITransaction transaction, boolean wasBought, boolean wasSold, boolean read, boolean added)
	{
		transaction_ = transaction;
		wasBought_ = wasBought;
		wasSold_ = wasSold;
		read_ = read;
		added_ = added;
	}
	
	public ITransaction getTransaction()
	{
		return transaction_;
	}
	
	public boolean wasBought()
	{
		return wasBought_;
	}
	
	public boolean wasSold()
	{
		return wasSold_;
	}
	
	public boolean wasRead()
	{
		return read_;
	}
	
	public boolean wasAdded()
	{
		return added_;
	}
}
