
package car_scraper;



public class Main_Class 
{
    public static void main(String[] args)
    {
        HermitageToyota ht = new HermitageToyota();
        ht.findCars(0, 150000);
        ht.printCars();
    }
    
}
