package observer.pattern;
import java.util.ArrayList;

import transaction.ITransaction;

public abstract class Subject
{
	// All observers subscribed for notifications
	private ArrayList<IObserver> observers_;
	
	public Subject()
	{
		observers_ = new ArrayList<IObserver>();
	}
	
	// Subscribe to notifications
	public void subscribe(IObserver o)
	{
		observers_.add(o);
	}
	
	// Unsubscribe from notifications
	public void unsubscribe(IObserver o)
	{
		observers_.remove(o);
	}
	
	// Notify all observers based on their filter
	public void notifyObservers(ITransaction transaction)
	{
		for(IObserver o : observers_)
		{
			if(o.validateUpdate(transaction))
				o.update(transaction);
		}
	}
}
