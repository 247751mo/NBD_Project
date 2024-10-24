package model;
import jakarta.persistence.*;


@Entity
@Access(AccessType.FIELD)
@DiscriminatorValue("Monthly")
public class Monthly extends Publication {

    public Monthly(String title, String genre, String publisher) {
        super(title, genre, publisher);
    }

    public Monthly() {
        super();
    }

    @Override
    public String volumeInfo() {
        return "Publication: " + super.volumeInfo() + ", publisher: " + getPublisher();
    }
}
