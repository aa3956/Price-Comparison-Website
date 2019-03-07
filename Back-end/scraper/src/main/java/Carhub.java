import java.io.IOException;
import static java.lang.Thread.sleep;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Carhub extends WebScraper {
  
    @Override
    public void run(){
        runThread = true;
        
        //While loop will keep running until runThread is set to false;
        while(runThread){
            System.out.println("Carhub thread is scraping data.");
            
            Document document2 = null;
            try {
                   document2 = Jsoup.connect("https://www.carhublondon.co.uk/used-cars").timeout(6000).get();

            } catch (IOException ex) {
                  Logger.getLogger(Carhub.class.getName()).log(Level.SEVERE, null, ex);
                } 
            
            //Grab the div that wraps the data
            Elements ele = document2.select("section.car-search-results");
            for (Element row : ele.select(".end")) {
                
                //Grap elements you want to scrape from each row
                String carUrl = row.select(".car-search-excerpt a").attr("href");
                String imageUrl = row.select(".car-picture-title-wrap").attr("style");
                String description = row.select(".title-row").text();
                String price = row.select(".price-row").text();
                
                //Split the URL and add it to the database table
                try {
                    URL urlSplit = new URL("https://www.carhublondon.co.uk".concat(carUrl));
                    
                    //Create a Url class instance
                    Url url = new Url();
                    url.setDomain(urlSplit.getHost());
                    url.setPath(urlSplit.getPath());
                    url.setQuery_String("Null");
                
                    //Save Url in carDao
                    carDao.addUrl(url);
                    
                    //Create an istance of the CarXML
                    CarXML car = new CarXML();

                    //Set values of Car class that we want to add
                    car.setImage_url(imageUrl);
                    car.setDescription(description);
                    car.setFeatures("");
                    car.setPrice(price);
                    car.setUrl(url);

                    //Save car in carDao
                    carDao.saveCar(car);

                } catch (MalformedURLException ex) {
                    Logger.getLogger(Carhub.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                //Print result to console
                System.out.println("Car Url: " + carUrl + "\n" +
                        "Image_url: " + imageUrl + "\n" +
                        "Description: " + description + "\n" + 
                        "Price: " + price + "\n"
                );
            }
            
            //Sleep for the crawl delay, which is in seconds
            try{
                sleep(1000 * crawlDelay);//Sleep is in milliseconds, so we need to multiply the crawl delay by 1000
            }
            catch(InterruptedException ex){
                System.err.println(ex.getMessage());
            }
        }                   
    }
}
