package validators;

import java.util.ArrayList;

import transaction.ITransaction;

public class InterestingCompaniesValidator implements IValidator
{
	private ArrayList<String> companies_;
	
	public InterestingCompaniesValidator(ArrayList<String> comp)
	{
		companies_ = comp;
	}
	
	@Override
	public boolean validate(ITransaction transaction)
	{
		for(String company : companies_)
		{
			if(company.equals(transaction.getClientName()))
				return true;
		}
		
		return false;
	}
}
