package model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;

@Getter

@NoArgsConstructor
public class Renter {

    @BsonProperty("personalID")
    private String personalID;

    @BsonProperty("firstName")
    @Setter
    private String firstName;

    @BsonProperty("lastName")
    @Setter
    private String lastName;

    @BsonProperty("renterType")
    @Setter
    private RenterType renterType;

    @BsonProperty("isArchived")
    @Setter
    private boolean isArchived;

    @BsonProperty("rents")
    @Setter
    private int rents;

    @BsonCreator
    public Renter(@BsonProperty("personalID") String personalID,
                  @BsonProperty("firstName") String firstName,
                  @BsonProperty("lastName") String lastName,
                  @BsonProperty("renterType") RenterType renterType) {
        this.personalID = personalID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.renterType = renterType;
        this.isArchived = false;
        this.rents = 0;
    }
    public int getMaxVolumes(int volumes) {
        int maxVolumes = this.renterType.maxVolumes(volumes);

        if (this.rents >= maxVolumes) {
            return maxVolumes;
        }

        return Math.min(maxVolumes, volumes);
    }
}
