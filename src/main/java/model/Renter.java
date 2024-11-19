package model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;

@Getter
@Setter
@NoArgsConstructor
public class Renter {

    @BsonId
    private String id;

    @BsonProperty("firstName")
    private String firstName;

    @BsonProperty("lastName")
    private String lastName;

    @BsonProperty("personalID")
    private String personalID;

    @BsonProperty("renterType")
    private RenterType renterType;

    @BsonProperty("isArchived")
    private boolean isArchived;

    @BsonProperty("rents")
    private int rents;

    @BsonCreator
    public Renter(@BsonProperty("id") String id,
                  @BsonProperty("firstName") String firstName,
                  @BsonProperty("lastName") String lastName,
                  @BsonProperty("personalID") String personalID,
                  @BsonProperty("renterType") RenterType renterType) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.personalID = personalID;
        this.renterType = renterType;
        this.isArchived = false;
    }
}
