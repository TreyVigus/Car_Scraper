package car_scraper;

import static car_scraper.Dealer.getNumericPrice;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class HondaNorth extends Dealer {
    
    public HondaNorth()
    {
        super("https://www.hondanorth.net/search/used-butler-pa/?cy=16001&tp=used/");
    }

    @Override
    public void getCurrentPageCars(double low, double high)
    {
        super.getCurrentPageCars(low, high);
        Elements price_dds = doc.getElementsByClass("vehicle_price");
        Elements name_div_containers = doc.getElementsByClass("srp_sub_region2");
        for(int i = 0; i < price_dds.size(); i++)
        {
            double price = getNumericPrice(price_dds.get(i).html());
            if (price < low || price > high) 
            {
                break;
            }
            Element name_div = name_div_containers.get(i).getElementsByClass("srp_vehicle_title_container").get(0);
            String name = name_div.getElementsByTag("a").get(0).html();
            cars.add(new Car(name,price));
        }
    }
    
    @Override
    public String getNextURL()
    {
        Elements next_anchor_containers = doc.getElementsByClass("next");
        if(!next_anchor_containers.isEmpty())
        {
            Element next_anchor = next_anchor_containers.get(0).getElementsByTag("a").get(0);
            return next_anchor.attr("abs:href");
        }
        
        return null;
    }
    
}
