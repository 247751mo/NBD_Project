package model;

import exceptions.ParameterException;
import jakarta.persistence.*;
import jakarta.validation.Valid;

import java.util.UUID;

@Entity
@Valid
@Table(name = "Renter")
@Access(AccessType.FIELD)
public class Renter extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "personal_id", unique = true, nullable = false)
    private String personalID;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "renter_type_id")
    private RenterType renterType;

    @Column(name = "rents")
    private int rents = 0;


    @Column(name = "is_archived")
    private boolean isArchive;

    public Renter() {
    }

    // Constructor with parameters
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
        this.renterType = renterType != null ? renterType : new NoCard();  // Defaults to NoCard
        this.isArchive = false;
    }

    // Getters and setters
    public UUID getId() {
        return id;
    }
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

    public int getRents() {
        return rents;
    }
    public void setRents(int rents) {
        this.rents = rents;
    }

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

    public void incrementRentCount() {
        this.rents++;
    }
}
