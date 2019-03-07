
import java.util.ArrayList;


public class CarDaoTest extends CarDao {
    ArrayList<CarXML> carArrayList = new ArrayList();
    ArrayList<Url> urlArrayList = new ArrayList();
    
    public void saveCar(CarXML cx){
        carArrayList.add(cx);
    } 
    
    public void addUrl(Url url){
        urlArrayList.add(url);
    }
    
}
