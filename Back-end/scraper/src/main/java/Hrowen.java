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

public class Hrowen extends WebScraper {

    //Url for the website I will be scraping
    String webUrl;
    
    @Override
    public void run(){
        runThread = true;
        
        //While loop will keep running until runThread is set to false;
        while(runThread){
            
            for (int i = 1; i < 9; i++) {
            
                webUrl = "https://www.hrowen.co.uk/bentley/page-" + i + "/used-bentley/default/-1/-1.aspx";
                System.out.println("This is the foor looop; " + webUrl);
                
                System.out.println("Hrowen thread is scraping data.");

                Document document4 = null;
                try {
                       document4 = Jsoup.connect(webUrl).timeout(6000).get();

                } catch (IOException ex) {
                      Logger.getLogger(Hrowen.class.getName()).log(Level.SEVERE, null, ex);
                    }

                //Grab the div that wraps the data
                Elements ele = document4.select("div.grid");
                for (Element row : ele.select("article.used-list-vehicle")) {

                    //Grap elements you want to scrape from each row
                    String carUrl = row.select(".used-list-vehicle-image a").attr("href");
                    String imageUrl = row.select("img").attr("src");
                    String description = row.select(".list-title").text();
                    String features = row.select(".skinny.left").text();
                    String price = row.select(".list-price").text();

                    //Split the URL and add it to the database table
                    try {
                        URL urlSplit = new URL("https://www.hrowen.co.uk".concat(carUrl));
                        
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
                        Logger.getLogger(Hrowen.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    //Print result to console
                    System.out.println("HROWEN: Car Url: " + carUrl + "\n" +
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
