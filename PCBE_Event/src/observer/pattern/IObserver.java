package observer.pattern;
import transaction.ITransaction;
import validators.AllNotificationsValidator;
import validators.IValidator;

public abstract class IObserver
{
	// Each observer has a validator to filter unwanted notifications
	private IValidator validator_;
	
	// Constructor subscribes for all notifications
	public IObserver()
	{
		validator_ = new AllNotificationsValidator();
	}
	
	// Choose a new validator
	public void setValidator(IValidator v)
	{
		validator_ = v;
	}
	
	// Called when an interesting notification happens
	public abstract void update(ITransaction transaction);
	
	// Method for validating notification
	public boolean validateUpdate(ITransaction transaction)
	{
		return validator_.validate(transaction);
	}
}
