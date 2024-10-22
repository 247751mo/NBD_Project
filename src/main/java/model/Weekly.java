package model;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.DiscriminatorValue;
import javax.persistence.Entity;


@Entity
@Access(AccessType.FIELD)
@DiscriminatorValue("Weekly")
public class Weekly extends Publication {



    public Weekly(String title, String genre, String publisher) {
        super(title, genre, publisher);
    }

    @Override
    public String volumeInfo() {
        return "Publication: " + super.volumeInfo() + ", publisher: " + getPublisher();
    }
}
