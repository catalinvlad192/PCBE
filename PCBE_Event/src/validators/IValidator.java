package validators;

import observer.pattern.Notification;

public interface IValidator
{
	public boolean validate(Notification notification);
}
