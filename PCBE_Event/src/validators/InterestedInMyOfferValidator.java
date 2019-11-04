package validators;

import transaction.ITransaction;

public class InterestedInMyOfferValidator implements IValidator
{
	private String myName_;
	
	public InterestedInMyOfferValidator(String myName)
	{
		myName_ = myName;
	}

	public boolean validate(ITransaction transaction)
	{
		return myName_.equals(transaction.getClientName());
	}
}
