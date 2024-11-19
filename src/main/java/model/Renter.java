package model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;

@Getter

@NoArgsConstructor
public class Renter {

    @BsonId
    private String id;

    @BsonProperty("firstName")
    @Setter
    private String firstName;

    @BsonProperty("lastName")
    @Setter
    private String lastName;

    @BsonProperty("personalID")
    @Setter
    private String personalID;

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
