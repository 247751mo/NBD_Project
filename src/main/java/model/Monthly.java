package model;
import jakarta.persistence.*;
import javax.persistence.Entity;


@Entity
@Access(AccessType.FIELD)
@DiscriminatorValue("Monthly")
public class Monthly extends Publication {

    public Monthly(String title, String genre, String publisher) {
        super(title, genre, publisher);
    }

    @Override
    public String volumeInfo() {
        return "Publication: " + super.volumeInfo() + ", publisher: " + getPublisher();
    }
}
