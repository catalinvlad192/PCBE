package validators;

import observer.pattern.Notification;

public class AllNotificationsValidator implements IValidator
{
	@Override
	public boolean validate(Notification notification)
	{
		return true;
	}
}
