package model;

import com.datastax.oss.driver.api.mapper.annotations.ClusteringColumn;
import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.Entity;
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey;
import lombok.Getter;

@Entity(defaultKeyspace = "rent_a_volume")
@CqlName("rents_by_renter")
@Getter

public class RentsByRenter extends Rent{
    @CqlName("rent_id")
    @ClusteringColumn
    private long rentID;
    @CqlName("personal_id")
    @PartitionKey
    private long personalID;
    private String startDate;
    private Volume volume;
    private Renter renter;
    private long volumeID;

    public RentsByRenter() {}

    public RentsByRenter(long rentID, String startDate, Volume volume, Renter renter) {
        super(rentID, startDate, volume, renter);
    }

    public RentsByRenter(long rentID, String startDate, long volumeID, long personalID) {
       super(rentID, startDate, volumeID, personalID);
    }
}
