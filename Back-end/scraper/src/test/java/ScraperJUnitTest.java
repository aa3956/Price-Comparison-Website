import org.junit.Test;
import static org.junit.Assert.*;

public class ScraperJUnitTest  {
    
    @Test
    public void cargiant() throws InterruptedException{
        CarDaoTest ct = new CarDaoTest();
        Cargiant cargiant = new Cargiant();
        
        cargiant.setCarDao(ct);
        cargiant.start();
        cargiant.stopThread();
        cargiant.join();
        
        assertTrue(ct.carArrayList.size() > 0);
    }
    
    @Test
    public void carhub() throws InterruptedException{
        CarDaoTest ct = new CarDaoTest();
        Carhub carhub = new Carhub();
        
        carhub.setCarDao(ct);
        carhub.start();
        carhub.stopThread();
        carhub.join();
        
        assert(ct.carArrayList.size() > 0);
    }
    
    @Test
    public void classicCars() throws InterruptedException{
        CarDaoTest ct = new CarDaoTest();
        Classiccars cls = new Classiccars();
        
        cls.setCarDao(ct);
        cls.start();
        cls.stopThread();
        cls.join();
        
        assert(ct.carArrayList.size() > 0);
    }
    
    @Test
    public void hrowen() throws InterruptedException{
        CarDaoTest ct = new CarDaoTest();
        Hrowen hr = new Hrowen();
        
        hr.setCarDao(ct);
        hr.start();
        hr.stopThread();
        hr.join();
        
        assert(ct.carArrayList.size() > 0);
    }
    
    @Test
    public void lamborghini() throws InterruptedException{
        CarDaoTest ct = new CarDaoTest();
        Lamborghini lam = new Lamborghini();
        
        lam.setCarDao(ct);
        lam.start();
        lam.stopThread();
        lam.join();
        
        assert(ct.carArrayList.size() > 0);
    }
    
     
}
