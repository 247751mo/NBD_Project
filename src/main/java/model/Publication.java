package model;

import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity(defaultKeyspace = "rent_a_volume")
@CqlName("volumes")
@Data
public class Publication extends Volume {

    private String publisher;

    public Publication(long volumeID, String title, String genre, String publisher) {
        super(volumeID, title, genre);
        this.publisher = publisher;
    }

    public Publication() {

    }


}
