package validators;

import transaction.ITransaction;

public class AllNotificationsValidator implements IValidator
{
	@Override
	public boolean validate(ITransaction transaction)
	{
		return true;
	}
}
