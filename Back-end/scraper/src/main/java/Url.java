import javax.persistence.*;

@Entity
@Table(name="url")
//Url table from mysql database
public class Url {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    int id;
    
    @Column(name = "domain")
    String domain;
    
    @Column(name = "path")
    String path;
    
    @Column(name = "query_string")
    String query_string;
    
    //Getters and setters for my variables
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getQuery_String() {
        return query_string;
    }

    public void setQuery_String(String query_string) {
        this.query_string = query_string;
    }
    

    @Override
    public String toString() {
        return "Url{" + "id=" + id + ", domain=" + domain + ", path=" + path + ", query_string=" + query_string + '}';
    }
  
}

