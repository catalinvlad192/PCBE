package observer.pattern;

import java.util.HashMap;

import main.FilePrinter;

public abstract class ISubject
{
	// All observers subscribed for notifications
	private HashMap<String, IObserver> observers_;
	
	public ISubject()
	{
		observers_ = new HashMap<String, IObserver>();
	}
	
	// Subscribe to notifications
	public void subscribe(IObserver o)
	{
		System.out.println("[Subject] Subscribe: " + o.getName());
		FilePrinter.printLine("[Subject] Subscribe: " + o.getName());
		observers_.put(o.getName(), o);
	}
	
	// Unsubscribe from notifications
	public void unsubscribe(IObserver o)
	{
		System.out.println("[Subject] Unsubscribe: " + o.getName());
		FilePrinter.printLine("[Subject] Unsubscribe: " + o.getName());
		observers_.remove(o.getName());
	}
	
	// Notify all observers based on their filter
	public void notifyObservers(Notification n)
	{
		System.out.println("[Subject] Notifying all observers");
		FilePrinter.printLine("[Subject] Notifying all observers");
		for(IObserver o : observers_.values())
		{
			if(o.validateUpdate(n))
				o.update(n);
		}
	}
}
