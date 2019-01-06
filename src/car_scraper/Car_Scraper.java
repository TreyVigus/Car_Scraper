package car_scraper;

import java.io.File;
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
        List<Car> hermitageToyota = getHermitageToyotaCars(3000,12000);
        for(Car c: hermitageToyota)
        {
            System.out.println(c);
        }
    }
    
    //get hermitage toyota cars in the given price range
    public static List<Car> getHermitageToyotaCars(double low, double high)
    {
        List<Car> cars = new ArrayList<>();
        try
        {
            Document doc = Jsoup.connect("https://www.hermitagetoyota.com/search/used/tp/").get();
            Elements car_wrappers = doc.getElementsByClass("srp_vehicle_wrapper srp_vehicle_item_container");
            
            for(Element e: car_wrappers)
            {
                //get the name
                Elements titleBar = e.getElementsByClass("srp_vehicle_titlebar");
                Elements a = titleBar.get(0).getElementsByTag​("a");
                String name = a.get(0).html();
                
                //get the smallest price that is either "Retail Price" or "Internet Price" (Sometimes there's a third price representing the different between these).
                Elements price_container = e.getElementsByClass("veh_pricing_inner_container details-price");
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
        }
        catch(Exception e)
        {
            
        }
        
        return cars;
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
