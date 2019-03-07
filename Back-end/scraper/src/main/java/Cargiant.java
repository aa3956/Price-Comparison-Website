import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Cargiant extends WebScraper {
    
    //Url for the website I will be scraping
    String webUrl;
    
    @Override
    public void run(){
        runThread = true;
        
        while(runThread){
        
            //Scrape data from pages 1-5
            for (int i = 1; i < 6; i++) {
                
                //webUrl add the incrementation of pages to the link
                webUrl = "https://www.cargiant.co.uk/search/all/all/page/" +  i;
                System.out.println("This is the foor looop; " + webUrl);

                System.out.println("Cargiant thread is scraping data.");
                
                //Document 1 to scrape data from
                Document document1 = null;
                  try {
                       document1 = Jsoup.connect(webUrl).timeout(6000).get();

                  } catch (IOException ex) {
                      Logger.getLogger(Cargiant.class.getName()).log(Level.SEVERE, null, ex);
                    }
                  
                //Grab the div that wraps the data
                Elements ele = document1.select("section.results");
                for (Element row : ele.select(".vehicle")) {

                    //Grap elements you want to scrape from each row
                    String carUrl = row.select(".vehicle-cover").attr("href");
                    String imageUrl = row.select(".image").attr("style");
                    String description = row.select(".small-7").text();
                    String features = row.select(".car-features").text();
                    String price = row.select(".car-price").text();

                    //Split the URL and add it to the database table
                    try {
                        URL urlSplit = new URL("https://www.cargiant.co.uk".concat(carUrl));
                        
                        //Create a Url class instance
                        Url url = new Url();
                        url.setDomain(urlSplit.getHost());
                        url.setPath(urlSplit.getPath());
                        url.setQuery_String("");
                    
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
                        Logger.getLogger(Cargiant.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    //Print results to console
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