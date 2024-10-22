package model;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "monthlies")
public class Monthly extends Publication {

    public Monthly(String title, String genre, String publisher) {
        super(title, genre, publisher);
    }

    @Override
    public String volumeInfo() {
        return "Publication: " + super.volumeInfo() + ", publisher: " + getPublisher();
    }
}
