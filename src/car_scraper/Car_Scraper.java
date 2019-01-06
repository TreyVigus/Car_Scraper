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
          List<Car> cars = getHermitageToyotaCars(0, 1200000);
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
            
            Elements car_wrappers = doc.getElementsByClass("srp_vehicle_wrapper srp_vehicle_item_container");
            for(Element e: car_wrappers)
            {
                //get the name
                Elements titleBar = e.getElementsByClass("srp_vehicle_titlebar");
                Elements a = titleBar.get(0).getElementsByTag​("a");
                String name = a.get(0).html();
                
                //get the smallest price that is either "Retail Price" or "Internet Price" (Sometimes there's a third price representing the different between these).
                Elements price_container = e.getElementsByClass("veh_pricing_inner_container details-price");
                if(price_container.isEmpty()) continue; //ignore the car_wrapper if it has no prices
                Elements dt = price_container.get(0).getElementsByTag("dt");
                Elements dd = price_container.get(0).getElementsByTag("dd");
                
                
                double min_price = Double.MAX_VALUE;
                for(int i = 0; i < dt.size(); i++)
                {
                    if(dt.get(i).html().equals("Retail Price") || dt.get(i).html().equals("Internet Price"))
                    {
                        double price = getNumericPrice(dd.get(i).html());
                        if(price < min_price)
                        {
                            min_price = price;
                        }
                    }
                }
                if(min_price >= low && min_price <= high)
                {
                    cars.add(new Car(name,min_price));
                }
            }
            
            return getNextHermitageToyotaURL(doc);
        }
        catch(Exception e)
        {
            return null;
        }
        
    }
    
    //TODO: what does this do when the last page is reached?
    //return the url of the next link in the top right corner in hermitage toyota
    public static String getNextHermitageToyotaURL(Document doc)
    {
        Elements page_container = doc.getElementsByClass("search-pagination fl_r");
        Elements next_li = page_container.get(0).getElementsByClass("next");
        if(next_li.isEmpty()) return null; //in case of last page, return null because there isn't a next page.
        Elements next_a = next_li.get(0).getElementsByTag("a");
        return next_a.attr("abs:href");
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