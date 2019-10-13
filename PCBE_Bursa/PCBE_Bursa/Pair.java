

public class Pair<T, Q>
{
	private T first_;
	private Q second_;

	public Pair(T t, Q q)
	{
		first_ = t;
		second_ = q;
	}

	public T first()
	{
		return first_;
	}

	public Q second()
	{
		return second_;
	}
}
