package model;

import com.datastax.oss.driver.api.mapper.annotations.ClusteringColumn;
import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey;
import exceptions.ParameterException;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.UUID;



@Entity(defaultKeyspace = "rent_a_volume")
@CqlName("volumes")
@Data
@NoArgsConstructor
public class Volume {
    @PartitionKey
    @CqlName("volume_id")
    private long volumeId;

    private String title;
    private String genre;
    private boolean isRented = false;
    public Volume(long volumeId, String title, String genre) {
        this.volumeId = volumeId;
        this.title = title;
        this.genre = genre;
    }



}
