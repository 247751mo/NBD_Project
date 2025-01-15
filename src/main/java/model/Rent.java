package model;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Rent {
    private long rentID;
    private String startDate;
    private Volume volume;
    private Renter renter;
    private long volumeID;
    private long personalID;

    public Rent(long rentID, String startDate, Volume volume, Renter renter) {
        this.rentID = rentID;
        this.startDate = startDate;
        this.volume = volume;
        this.renter = renter;
        this.volumeID = volume.getVolumeId();
        this.personalID = renter.getPersonalId();
    }

    public Rent(long rentID, String startDate, long volumeID, long personalID) {
        this.rentID = rentID;
        this.startDate = startDate;
        this.volumeID = volumeID;
        this.personalID = personalID;
    }
}
