package validators;

import main.FilePrinter;
import observer.pattern.Notification;

public class PriceBetweenThresholdsValidator implements IValidator
{
	private int minPrice_;
	private int maxPrice_;
	
	public PriceBetweenThresholdsValidator(int min, int max)
	{
		minPrice_ = min;
		maxPrice_ = max;
	}

	@Override
	public boolean validate(Notification notification)
	{
		if(minPrice_ <= notification.getTransaction().getPrice() && maxPrice_ >= notification.getTransaction().getPrice()
				&& notification.wasAdded() && notification.getTransaction().isForSale())
		{
			System.out.println("[PriceBetweenThresholdsValidator] New offer that i can afford");
			FilePrinter.printLine("[PriceBetweenThresholdsValidator] New offer that i can afford");
			return true;
		}
		return false;
	}
}