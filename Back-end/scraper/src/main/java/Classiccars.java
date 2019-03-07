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

public class Classiccars extends WebScraper {
    
    String webUrl;
    
    @Override
    public void run(){
        runThread = true;
        
        //While loop will keep running until runThread is set to false;
        while(runThread){
            
            //Scrape data from pages 1-5
            for (int i = 1; i < 6; i++) {
                
                webUrl = "https://www.classiccarsukltd.co.uk/used-cars?page=" +  i;
                System.out.println("This is the foor looop; " + webUrl);
                
                System.out.println("Classiccars thread is scraping data.");

                Document document3 = null;
                try {
                       document3 = Jsoup.connect(webUrl).timeout(6000).get();

                } catch (IOException ex) {
                      Logger.getLogger(Classiccars.class.getName()).log(Level.SEVERE, null, ex);
                    }

               //Grab the div that wraps the data
                Elements ele = document3.select("ul.stock-items");
                for (Element row : ele.select(".stocklistAdvert")) {

                    //Grap elements you want to scrape from each row
                    String carUrl = row.select(".stocklistAdvert a").attr("href");
                    String imageUrl = row.select(".u-pos-r.u-z-1.lazy").attr("data-src");
                    String description = row.select(".stocklistAdvert__title").text();
                    String features = row.select(".stocklistAdvert__summary").text();
                    String price = row.select(".stocklistAdvert__price").text();

                    //Split the URL and add it to the database table
                    try {
                        URL urlSplit = new URL("https://www.classiccarsukltd.co.uk".concat(carUrl));
                        
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
                        car.setFeatures(features);
                        car.setPrice(price);
                        car.setUrl(url);
    
                        //Save car in carDao
                        carDao.saveCar(car);
    
                    } catch (MalformedURLException ex) {
                        Logger.getLogger(Classiccars.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    //Print result to console
                    System.out.println("Car Url: " + carUrl + "\n" +
                            "Image_url: " + imageUrl + "\n" +
                            "Description: " + description + "\n" +
                            "Features: " + features + "\n" + 
                            "Price: " + price + "\n"
                    );
                }
            }
            //Sleep for the crawl delay, which is in seconds
            try{
                sleep(1000 * crawlDelay);//Sleep is in milliseconds, so we need to multiply the crawl delay by 1000
            }
            catch(InterruptedException ex){
                System.err.println(ex.getMessage());
            }
            break;
        }
    }   
}
