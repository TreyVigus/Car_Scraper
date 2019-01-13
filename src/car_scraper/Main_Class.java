
package car_scraper;



public class Main_Class 
{
    public static void main(String[] args)
    {
        HermitageToyota ht = new HermitageToyota();
        ht.findCars(0, 8000);
        HondaNorth hn = new HondaNorth();
        hn.findCars(0, 8000);
        
        //SendEmail.send("trey.vigus@gmail.com", "trey.vigus@gmail.com", "test", "it worked!");
    }
    
}
