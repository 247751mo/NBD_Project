package model;

import exceptions.ParameterException;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import static java.util.Objects.requireNonNullElseGet;

public class Renter {
    private String firstName;
    private String lastName;
    private String personalID;
    private RenterType renterType;
    private boolean isArchive;

    // Constructor
    public Renter(String firstName, String lastName, String personalID, RenterType renterType) {
        if (firstName == null || firstName.isEmpty()) {
            throw new ParameterException("Invalid firstName (can't be empty)!");
        }
        if (lastName == null || lastName.isEmpty()) {
            throw new ParameterException("Invalid lastName (can't be empty)!");
        }
        if (personalID == null || personalID.isEmpty()) {
            throw new ParameterException("Invalid personalID (can't be empty)!");
        }
        this.firstName = firstName;
        this.lastName = lastName;
        this.personalID = personalID;
        this.renterType = requireNonNullElseGet(renterType, NoCard::new);
        this.isArchive = false;
    }

    // Destructor equivalent in Java is the finalize method but it's not typically used.
    // Instead, Java uses garbage collection.

    // Getters
    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPersonalID() {
        return personalID;
    }

    public RenterType getType() {
        return renterType;
    }

    // Setters
    public void setFirstName(String newFirstName) {
        if (newFirstName != null && !newFirstName.isEmpty()) {
            this.firstName = newFirstName;
        }
    }

    public void setLastName(String newLastName) {
        if (newLastName != null && !newLastName.isEmpty()) {
            this.lastName = newLastName;
        }
    }

    public void setType(RenterType newRenterType) {
        if (newRenterType != null) {
            this.renterType = newRenterType;
        }
    }

    public void setArchiveStatus(boolean status) {
        this.isArchive = status;
    }

    // Other methods
    public String getInfo() {
        return "(Renter) first name: " + getFirstName() + ", last name: " + getLastName() + ", personal id: " + getPersonalID() +
                ", renter type: " + getType().getRenterTypeInfo() + ", is archived: " + (checkIfArchived() ? "true" : "false");
    }

    public boolean checkIfArchived() {
        return isArchive;
    }

    public int maxVolumes(int volumes) {
        return renterType.maxVolumes(volumes);
    }
}
