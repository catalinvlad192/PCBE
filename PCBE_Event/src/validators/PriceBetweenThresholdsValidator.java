package validators;

import transaction.ITransaction;

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
	public boolean validate(ITransaction transaction)
	{
		if(minPrice_ <= transaction.getPrice() || maxPrice_ >= transaction.getPrice())
			return true;
		return false;
	}
}