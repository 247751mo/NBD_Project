package managers;


import model.Renter;
import model.Volume;
import repositories.MongoRentRepo;

import java.time.LocalDateTime;

public class RentManager {
    private final MongoRentRepo rentRepo;


    public RentManager(MongoRentRepo rentRepo) {
        if (rentRepo == null) {
            throw new IllegalArgumentException("rentRepo cannot be null");
        }
        this.rentRepo = rentRepo;
    }

    public void rentVolume(Renter renter, Volume volume, LocalDateTime rentStart) throws Exception {
        if (renter == null) {
            throw new IllegalArgumentException("Renter cannot be null.");
        }
        if (volume == null) {
            throw new IllegalArgumentException("Volume cannot be null.");
        }

        if (volume.getIsRented()==1) {
            throw new Exception("Volume is already rented: " + volume.getTitle());
        }

        if (renter.getCurrentRentsNumber() >= 5) {
            throw new Exception("Renter has reached the maximum number of rents: " + renter.getPersonalID());
        }


    }

}