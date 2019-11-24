package validators;

import java.util.ArrayList;

import main.FilePrinter;
import observer.pattern.Notification;

public class InterestingCompaniesValidator implements IValidator
{
	private ArrayList<String> companies_;
	
	public InterestingCompaniesValidator(ArrayList<String> comp)
	{
		companies_ = comp;
	}
	
	@Override
	public boolean validate(Notification notification)
	{
		for(String company : companies_)
		{
			if(company.equals(notification.getTransaction().getClientName())
					&& notification.wasAdded())
			{
				System.out.println("[InterestingCompaniesValidator] Company: " + company + " created an offer ");
				FilePrinter.printLine("[InterestingCompaniesValidator] Company: " + company + " created an offer ");
				return true;
			}
		}
		
		return false;
	}
}
