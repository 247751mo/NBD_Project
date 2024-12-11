package model;

import jakarta.json.bind.annotation.JsonbCreator;
import jakarta.json.bind.annotation.JsonbProperty;
import lombok.Getter;
import lombok.Setter;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.Objects;

@Getter
public class Renter {

    @BsonId
    @JsonbProperty("personalID")
    private String personalID;

    @JsonbProperty("firstName")
    @BsonProperty("firstName")
    @Setter
    private String firstName;

    @JsonbProperty("lastName")
    @BsonProperty("lastName")
    @Setter
    private String lastName;

    @JsonbProperty("isArchived")
    @BsonProperty("isArchived")
    @Setter
    private boolean isArchived;

    @Setter
    @BsonProperty("currentRentsNumber")
    @JsonbProperty("currentRentsNumber")
    private int currentRentsNumber;

    @BsonCreator
    @JsonbCreator
    public Renter(@BsonProperty("personalID") @JsonbProperty("personalID") String personalID,
                  @BsonProperty("firstName") @JsonbProperty("firstName") String firstName,
                  @BsonProperty("lastName") @JsonbProperty("lastName") String lastName){
        this.personalID = personalID;
        this.firstName = firstName;
        this.lastName = lastName;
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


}
