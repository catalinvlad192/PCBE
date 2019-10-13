
public class Utils
{
    public static boolean checkOfferRequestStocks(Stocks offer, Stocks request)
    {
        if (!offer.getCompanyName().equals(request.getCompanyName()) && offer.isForSale() && request.isForSale())
        {
            Pair<int, int> o = offer.getStocksAndPrice();
            Pair<int, int> r = request.getStocksAndPrice();

            if (o.first() >= r.first() && o.second() <= r.second())
            {
                return true;
            }
        }
        return false;
    }

    public static boolean makeTransaction(Client seller, Client buyer, Stocks offer, Stocks request)
    {
        if (seller.getCompanyName().equals(offer.getCompanyName()) && 
            buyer.getCompanyName().equals(request.getCompanyName()))
        {
            Pair<int, int> o = offer.getStocksAndPrice();
            Pair<int, int> r = request.getStocksAndPrice();

            seller.substract
        }
        return false;
    }
}
