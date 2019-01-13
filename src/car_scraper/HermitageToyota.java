package car_scraper;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class HermitageToyota extends Dealer {
    
    
    public HermitageToyota()
    {
        super("https://www.hermitagetoyota.com/search/used/tp/");
    }

    @Override
    public void getCurrentPageCars(double low, double high)
    {
        super.getCurrentPageCars(low, high);
        Elements price_spans = doc.getElementsByAttributeValue("itemprop", "price");
        Elements name_spans = doc.getElementsByAttributeValue("itemprop", "name");
        //if (name_spans.size() != price_spans.size()) return;   had to remove this because page 9 was missing. this doesnt cause an issue because the unpriced entries are always last
        
        for (int i = 0; i < price_spans.size(); i++) 
        {
            double price = getNumericPrice(price_spans.get(i).html());
            if (price < low || price > high) 
            {
                break;
            }

            String date = name_spans.get(i).getElementsByAttributeValue("itemprop", "vehicleModelDate").get(0).html();
            String brand = name_spans.get(i).getElementsByAttributeValue("itemprop", "brand").get(0).html();
            String model = name_spans.get(i).getElementsByAttributeValue("itemprop", "model").get(0).html();
            String config = name_spans.get(i).getElementsByAttributeValue("itemprop", "vehicleConfiguration").get(0).html();

            cars.add(new Car(date + " " + brand + " " + model + " " + config, price));
        }

    }
    
    @Override
    //return the url of the next link in the top right corner in hermitage toyota
    public String getNextURL()
    {
        Element anchors_div = doc.getElementsByClass("shopping").get(0);
        Element current_anchor = anchors_div.getElementsByAttributeValue("href", "Javascript:;").get(0);
        Element next_anchor = current_anchor.nextElementSibling​();
        if(next_anchor != null)
            return next_anchor.attr("abs:href");
        
        return null;
    }
    
}
