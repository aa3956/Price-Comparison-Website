import java.util.List;
import java.util.Scanner;

/** Starts and stops the web scrapers */
public class ScraperManager {
    List<WebScraper> scraperList;

    //Starts up the scrapers 
    public void startScraping(){
        //Start the web scrapers
        for(WebScraper webScraper : scraperList){
            webScraper.start();
        }
        
        //Wait until the user types stop
        Scanner scanner = new Scanner(System.in);
        String userInput = scanner.nextLine();
        while(!userInput.equals("stop")){
            userInput = scanner.nextLine();
        }
        
        //Wait for web scrapers to stop
        for(WebScraper webScraper : scraperList){
            try{
                webScraper.stopThread();
                webScraper.join();
            }
            catch(Exception ex){
                ex.printStackTrace();
            }
        }
        
        //Shut down sessionManager - only one of these is shared between the scrapers
        if(!scraperList.isEmpty())
            scraperList.get(0).getCarDao().getSessionFactory().close();
        
        System.out.println("Scraping terminated");;
    }

    
    //Getters and setters
    public List<WebScraper> getScraperList() {
        return scraperList;
    }
    public void setScraperList(List<WebScraper> scraperList) {
        this.scraperList = scraperList;
    }
}
