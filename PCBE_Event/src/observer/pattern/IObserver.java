package observer.pattern;

import validators.AllNotificationsValidator;
import validators.IValidator;

public abstract class IObserver
{
	// Each observer has a validator to filter unwanted notifications
	private IValidator validator_;
	private String observerName_;
	
	// Constructor subscribes for all notifications
	public IObserver(String name)
	{
		validator_ = new AllNotificationsValidator();
		observerName_ = name;
	}
	
	// Choose a new validator
	public void setValidator(IValidator v)
	{
		validator_ = v;
	}
	
	public String getName()
	{
		return observerName_;
	}
	
	// Called when an interesting notification happens
	public abstract void update(Notification notification);
	
	// Method for validating notification
	public boolean validateUpdate(Notification notification)
	{
		return validator_.validate(notification);
	}
}
