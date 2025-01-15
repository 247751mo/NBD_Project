package model;

import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.Entity;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity(defaultKeyspace = "rent_a_volume")
@CqlName("volumes")
@Data
public class Publication extends Volume {

    private String publisher;

    public Publication(long volumeID, String discriminator, String title, String genre, String publisher, boolean rented) {
        super(volumeID, discriminator, title, genre, false);
        this.publisher = publisher;
    }

    public Publication() {

    }


}
