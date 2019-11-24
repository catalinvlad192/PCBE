package validators;

import main.FilePrinter;
import observer.pattern.Notification;

public class InterestedInMyOfferValidator implements IValidator
{
	private String myName_;
	
	public InterestedInMyOfferValidator(String myName)
	{
		myName_ = myName;
	}

	public boolean validate(Notification notification)
	{
		if(myName_.equals(notification.getTransaction().getClientName())
				&& notification.wasRead())
		{
			System.out.println("[InterestedInMyOfferValidator] My offer was read");
			FilePrinter.printLine("[InterestedInMyOfferValidator] My offer was read");
			return true;
		}
		return false;
	}
}
