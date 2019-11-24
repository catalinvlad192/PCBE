package validators;

import observer.pattern.Notification;

public class SoldValidator implements IValidator
{

	@Override
	public boolean validate(Notification notification) {
		if (notification.getTransaction().isForSale() && notification.wasSold())
		{
			return true;
		}
		return false;
	}

}
