
public class Stocks
{
    private String company_;
    private int numberOfStocks_;
    private int dollarsPerAction_;
    private boolean forSale_;

    public Stocks(String company, int numberOfStocks, int dollarsPerAction, boolean forSale)
    {
        company_ = company;
        numberOfStocks_ = numberOfStocks;
        dollarsPerAction_ = dollarsPerAction;
        forSale_ = forSale;
    }

    public boolean isForSale()
    {
        return forSale_;
    }

    public Pair<int, int> getStocksAndPrice()
    {
        Pair<int, int> pair = new Pair<int, int>(numberOfStocks_, dollarsPerAction_);
        return pair;
    }

    public String getCompanyName()
    {
        return company_;
    }
}
