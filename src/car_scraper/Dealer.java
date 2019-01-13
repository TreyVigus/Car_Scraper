package car_scraper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public abstract class Dealer 
{
    protected String url; //url of first page to start search on
    protected List<Car> cars;
    protected Document doc;
    
    public Dealer(String url)
    {
        this.url = url;
        cars = new ArrayList<>();
    }
    
    
    //get cars in a given price range using jsoup
    public final List<Car> findCars(double low, double high)
    {
        while(url != null)
        {
            getCurrentPageCars(low, high);
            url = getNextURL();
        }
        printCars();
        return cars;
    }
    
    
    //Override this: get all the cars on the current page and store them in the cars array list
    public void getCurrentPageCars(double low, double high)
    {
        try
        {
            doc = Jsoup.connect(url).get();
        }
        catch(IOException e)
        {
            System.out.println("problem with url");
        }
    }
    
    
    //get the url of the next page we need to process
    //should return null when there is no next URL
    public abstract String getNextURL();
    
    
    
    //strip unnecessary characters from the price (e.g. $) and convert to double
    //TODO: generalize this for multiple sites, maybe use regex?
    public static double getNumericPrice(String price)
    {
        price = price.replace("$", "");
        price = price.replace(",", "");
        return Double.parseDouble(price);
    }
    
    public void printCars()
    {
        for(Car c: cars)
        {
            System.out.println(c.toString());
        }
    }
    
    
}
