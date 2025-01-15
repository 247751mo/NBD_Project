package model;

import com.datastax.oss.driver.api.mapper.annotations.ClusteringColumn;
import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.Entity;
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey;
import exceptions.ParameterException;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private String discriminator;
    private String title;
    private String genre;
    private boolean isRented = false;
    public Volume(long volumeId,String discriminator, String title, String genre) {
        this.volumeId = volumeId;
        this.discriminator = discriminator;
        this.title = title;
        this.genre = genre;
        this.isRented = false;
    }



}
