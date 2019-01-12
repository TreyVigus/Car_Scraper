package car_scraper;

import java.util.List;


public class Car_Scraper 
{

    public static void main(String[] args) 
    {
          List<Car> cars = HermitageToyota.getHermitageToyotaCars(0, 12000000);
          for(Car c: cars)
          {
              System.out.println(c.toString());
          }
    }
    
}