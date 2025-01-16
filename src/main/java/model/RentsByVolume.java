package model;

import com.datastax.oss.driver.api.mapper.annotations.ClusteringColumn;
import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.Entity;
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey;
import lombok.Getter;

@Entity(defaultKeyspace = "rent_a_volume")
@CqlName("rents_by_volume")
@Getter
public class RentsByVolume extends Rent{
    @CqlName("rent_id")
    @ClusteringColumn
    private long rentID;
    @CqlName("volume_id")
    @PartitionKey
    private long volumeID;
    private long personalID;
    private String startDate;
    private Volume volume;
    private Renter renter;

    public RentsByVolume() {}

    public RentsByVolume(long rentID, String startDate, Volume volume, Renter renter) {
        super(rentID, startDate, volume, renter);
    }

    public RentsByVolume(long rentID, String startDate, long volumeID, long personalID) {
        super(rentID, startDate, volumeID, personalID);
    }
}
