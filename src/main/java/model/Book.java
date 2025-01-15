package model;

import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(defaultKeyspace = "rent_a_volume")
@CqlName("volumes")
@Data
public class Book extends Volume {

    private String author;

    public Book(long volumeID,String author, String title, String genre) {
        super(volumeID, title, genre);
        this.author = author;
    }

    public Book() {

    }

}

