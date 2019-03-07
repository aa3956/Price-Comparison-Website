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

public class Rybrook extends WebScraper {

    @Override
    public void run(){
        runThread = true;
        
        //While loop will keep running until runThread is set to false;
        while(runThread){
                
            System.out.println("Rybrook thread is scraping data.");

            Document document6 = null;
            try {
                   document6 = Jsoup.connect("https://www.rybrook.co.uk/approved-used-cars/lamborghini/").timeout(6000).get();

            } catch (IOException ex) {
                  Logger.getLogger(Rybrook.class.getName()).log(Level.SEVERE, null, ex);
                }

            //Grab the div that wraps the data
            Elements ele = document6.select("#ajax-stock");
            for (Element row : ele.select(".ryb-used-car-item")) {

                //Grap elements you want to scrape from each row
                String carUrl = row.select(".image a").attr("href");
                String imageUrl = row.select(".main-image").attr("src");
                String description = row.select(".description a").text();
                String features = row.select("ul").text();
                String price = row.select(".call-to-action h3").text();

                //Split the URL and add it to the database table
                try {
                    URL urlSplit = new URL(carUrl);

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
                    Logger.getLogger(Rybrook.class.getName()).log(Level.SEVERE, null, ex);
                }

                //Print result to console
                System.out.println("Car Url: " + carUrl + "\n" +
                        "Image_url: " + imageUrl + "\n" +
                        "Description: " + description + "\n" +
                        "Features: " + features + "\n" + 
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


