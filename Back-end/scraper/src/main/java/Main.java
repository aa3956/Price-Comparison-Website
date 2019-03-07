import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


public class Main {
    public static void main(String[] args){

       //This runs the scrapers 
       runApplicationsAnnotationConfig();
    }
    
    /** Uses Spring XML configuration to set up and run application */
    static void runApplicationsAnnotationConfig(){
        
        //Instruct Spring to create and wire beans using XML file
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        
        //Get scraper bean
        ScraperManager scraperManager = (ScraperManager) context.getBean("scraperManager");
        
        //Call methods on scraper bean
        scraperManager.startScraping();
   }
}


