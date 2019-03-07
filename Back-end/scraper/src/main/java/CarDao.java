import org.hibernate.Session;
import org.hibernate.SessionFactory;


public class CarDao {
    SessionFactory sessionFactory;
    
    /** Adds a new car to the database */
    public void saveCar(CarXML car){
  
        //Get a new Session instance from the SessionFactory
        Session session = sessionFactory.getCurrentSession();

        //Start transaction
        session.beginTransaction();

        //Add Car to database - will not be stored until we commit the transaction
        session.save(car);

        //Commit transaction to save it to database
        session.getTransaction().commit();
        
        //Close the session and release database connection
        session.close();
        System.out.println("Car added to database with ID: " + car.getId());
    }
    
        /** Adds a new car to the database */
    public void addUrl(Url url){
  
        //Get a new Session instance from the SessionFactory
        Session session = sessionFactory.getCurrentSession();

        //Start transaction
        session.beginTransaction();

        //Add Car to database - will not be stored until we commit the transaction
        session.save(url);

        //Commit transaction to save it to database
        session.getTransaction().commit();
        
        //Close the session and release database connection
        session.close();
        System.out.println("Car added to database with ID: " + url.getId());
    }

    //Getter and Setter for the session factory
    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
}
