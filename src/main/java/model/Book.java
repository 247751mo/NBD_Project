package model;

import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.Entity;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(defaultKeyspace = "rent_a_volume")
@CqlName("volumes")
@Data
@NoArgsConstructor
public class Book extends Volume {

    private String author;

    public Book(long volumeID, String discriminator, String title, String genre, String author, boolean rented) {
        super(volumeID, discriminator, title, genre, false);
        this.author = author;
    }

}

