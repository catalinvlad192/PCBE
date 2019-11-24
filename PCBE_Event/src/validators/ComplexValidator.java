package validators;

import observer.pattern.Notification;

public class ComplexValidator implements IValidator
{
	IValidator v1_;
	IValidator v2_;
	
	public ComplexValidator(IValidator v1, IValidator v2)
	{
		v1_ = v1;
		v2_ = v2;
	}
	
	@Override
	public boolean validate(Notification notification) 
	{
		return v1_.validate(notification) || v2_.validate(notification);
	}

}
