package car_scraper;

import java.util.ArrayList;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class Car_Scraper 
{

    public static void main(String[] args) 
    {
          List<Car> cars = getHermitageToyotaCars(0, 12000000);
          for(Car c: cars)
          {
              System.out.println(c.toString());
          }
    }
    
    
    //get hermitage toyota cars in the given price range using jsoup to parse the html for each used car page.
    public static List<Car> getHermitageToyotaCars(double low, double high)
    {
        List<Car> cars = new ArrayList<>();
        String url = "https://www.hermitagetoyota.com/search/used/tp/"; //url of first page.
        while(url != null)
        {
            url = getCurrentPageHermitageToyotaCars(low, high, cars, url);
        }
        return cars;
    }
    
    
    //get all the cars on the current page and return the string for the next url
    public static String getCurrentPageHermitageToyotaCars(double low, double high, List<Car> cars, String url)
    {
        try
        {
            Document doc = Jsoup.connect(url).get();
            
            
            Elements name_spans = doc.getElementsByAttributeValue("itemprop", "name");
            Elements price_spans = doc.getElementsByAttributeValue("itemprop", "price");
            if(name_spans.size() != price_spans.size()) return null;
                
            for(int i = 0; i < name_spans.size(); i++)
            {
                double price = getNumericPrice(price_spans.get(i).html());
                if(price < low || price > high) break;
                
                String date = name_spans.get(i).getElementsByAttributeValue("itemprop", "vehicleModelDate").get(0).html();
                String brand = name_spans.get(i).getElementsByAttributeValue("itemprop", "brand").get(0).html();
                String model = name_spans.get(i).getElementsByAttributeValue("itemprop", "model").get(0).html();
                String config = name_spans.get(i).getElementsByAttributeValue("itemprop", "vehicleConfiguration").get(0).html();
                
                cars.add(new Car(date + " " + brand + " " + model + " " + config, price));
            }
            
            return getNextHermitageToyotaURL(doc);
        }
        catch(Exception e)
        {
            return null;
        }
        
    }
    
    //return the url of the next link in the top right corner in hermitage toyota
    public static String getNextHermitageToyotaURL(Document doc)
    {
        Element anchors_div = doc.getElementsByClass("shopping").get(0);
        Element current_anchor = anchors_div.getElementsByAttributeValue("href", "Javascript:;").get(0);
        Element next_anchor = current_anchor.nextElementSibling​();
        if(next_anchor != null)
            return next_anchor.attr("abs:href");
        
        return null;
    }
    
    
    //strip unnecessary characters from the price (e.g. $) and convert to double
    //TODO: generalize this for multiple sites, maybe use regex?
    public static double getNumericPrice(String price)
    {
        price = price.replace("$", "");
        price = price.replace(",", "");
        return Double.parseDouble(price);
    }
    
    
    
    
}
