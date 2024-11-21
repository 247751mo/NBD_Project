package model;

import lombok.Getter;
import lombok.Setter;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.Objects;

@Getter
public class Renter {

    @BsonId
    @Getter
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

    @BsonProperty("currentRentsNumber")
    @Setter
    private int currentRentsNumber;

    @BsonCreator
    public Renter(@BsonProperty("personalID") String personalID,
                  @BsonProperty("firstName") String firstName,
                  @BsonProperty("lastName") String lastName/*,
                  @BsonProperty("renterType") RenterType renterType*/){
        this.personalID = personalID;
        this.firstName = firstName;
        this.lastName = lastName;
      /*  this.renterType = renterType;*/
        this.isArchived = false;
        this.currentRentsNumber = 0;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Renter renter = (Renter) o;
        return isArchived == renter.isArchived &&
                currentRentsNumber == renter.currentRentsNumber &&
                Objects.equals(personalID, renter.personalID) &&
                Objects.equals(firstName, renter.firstName) &&
                Objects.equals(lastName, renter.lastName);

    }

    @Override
    public int hashCode() {
        return Objects.hash(personalID, firstName, lastName, isArchived, currentRentsNumber);
    }

    public int getMaxVolumes(int volumes) {
        int maxVolumes = this.renterType.maxVolumes(volumes);

        if (this.currentRentsNumber >= maxVolumes) {
            return maxVolumes;
        }

        return Math.min(maxVolumes, volumes);
    }
}
