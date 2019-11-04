package validators;

import transaction.ITransaction;

public interface IValidator
{
	public boolean validate(ITransaction transaction);
}
