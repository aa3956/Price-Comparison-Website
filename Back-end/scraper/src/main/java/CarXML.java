import javax.persistence.*;

@Entity
@Table(name="car")
//Car table from mysql
public class CarXML {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    int id;
    
    @Column(name = "image_url")
    String image_url;
    
    @Column(name = "description")
    String description;
    
    @Column(name = "features")
    String features;
    
    @Column(name = "price")
    String price;
    
    @ManyToOne
    @JoinColumn(name="url_id", nullable=false)
    Url url_id;
    
    //Getters and Setters for the table
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFeatures() {
        return features;
    }

    public void setFeatures(String features) {
        this.features = features;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Url getUrl() {
        return url_id;
    }

    public void setUrl(Url url_id) {
        this.url_id = url_id;
    }

    @Override
    public String toString() {
        return "Car{" + "id=" + id + ", image_url=" + image_url + ", description=" + description + 
                ", features=" + features + ", price=" + price + ", url=" + url_id + '}';
    }    
    
}
