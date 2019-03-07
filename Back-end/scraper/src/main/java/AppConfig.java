
import java.util.ArrayList;
import java.util.List;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    SessionFactory sessionFactory;
    
    //Create a bean instance of the ScraperManager class to run each one
    @Bean
    public ScraperManager scraperManager(){
        ScraperManager scraperManager = new ScraperManager();
        
        //Create list of web scrapers and add to scraper manager
        List<WebScraper> scraperList = new ArrayList();
        scraperList.add(cargiant());
        scraperList.add(carhub());
        scraperList.add(classicCars());
        scraperList.add(hrowen());
        scraperList.add(lamborghini());
        scraperList.add(rybrook());
        scraperManager.setScraperList(scraperList);

        return scraperManager;
    }
    
    //Create a bean instance of the Cargiant class
    @Bean
    public Cargiant cargiant(){
        Cargiant cargiant = new Cargiant();
        cargiant.setCarDao(carDao());
        cargiant.setCrawlDelay(1);
        return cargiant;
    }
    
    //Create a bean instance of the Carhub class
    @Bean
    public Carhub carhub(){
        Carhub carhub = new Carhub();
        carhub.setCarDao(carDao());
        carhub.setCrawlDelay(1);
        return carhub;
    }
    
    //Create a bean instance of the Classiccars class
    @Bean
    public Classiccars classicCars(){
        Classiccars classicCars = new Classiccars();
        classicCars.setCarDao(carDao());
        classicCars.setCrawlDelay(1);
        return classicCars;
    }
    
    //Create a bean instance of the Hrowen class
    @Bean
    public Hrowen hrowen(){
        Hrowen hrowen = new Hrowen();
        hrowen.setCarDao(carDao());
        hrowen.setCrawlDelay(1);
        return hrowen;
    }
    
    //Create a bean instance of the Lamborghini class
    @Bean
    public Lamborghini lamborghini(){
        Lamborghini lamborghini = new Lamborghini();
        lamborghini.setCarDao(carDao());
        lamborghini.setCrawlDelay(1);
        return lamborghini;
    }
    
    //Create a bean instance of the Lamborghini class
    @Bean
    public Rybrook rybrook(){
        Rybrook rybrook = new Rybrook();
        rybrook.setCarDao(carDao());
        rybrook.setCrawlDelay(1);
        return rybrook;
    }
    
    //Create a bean instance of the CarDao class
    @Bean
    public CarDao carDao(){
        CarDao carDao = new CarDao();
        carDao.setSessionFactory(sessionFactory());
        return carDao;
    }
    
    //Create a session factory bean
    @Bean
    public SessionFactory sessionFactory(){
        if(sessionFactory == null){//Build sessionFatory once only
            try {
                //Create a builder for the standard service registry
                StandardServiceRegistryBuilder standardServiceRegistryBuilder = new StandardServiceRegistryBuilder();

                //Load configuration from hibernate configuration file.
                //Here we are using a configuration file that specifies Java annotations.
                standardServiceRegistryBuilder.configure("resources/hibernate-annotations.cfg.xml"); 

                //Create the registry that will be used to build the session factory
                StandardServiceRegistry registry = standardServiceRegistryBuilder.build();
                try {
                    //Create the session factory - this is the goal of the init method.
                    sessionFactory = new MetadataSources( registry ).buildMetadata().buildSessionFactory();
                }
                catch (Exception e) {
                        /* The registry would be destroyed by the SessionFactory, 
                            but we had trouble building the SessionFactory, so destroy it manually */
                        System.err.println("Session Factory build failed.");
                        e.printStackTrace();
                        StandardServiceRegistryBuilder.destroy( registry );
                }
                //Ouput result
                System.out.println("Session factory built.");
            }
            catch (Throwable ex) {
                // Make sure you log the exception, as it might be swallowed
                System.err.println("SessionFactory creation failed." + ex);
                ex.printStackTrace();
            }
        }
        return sessionFactory;
    }
}
