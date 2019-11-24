package validators;

import observer.pattern.Notification;

public class BoughtValidator implements IValidator
{

	@Override
	public boolean validate(Notification notification) {
		if (notification.getTransaction().isForSale() && notification.wasBought())
		{
			return true;
		}
		return false;
	}
}