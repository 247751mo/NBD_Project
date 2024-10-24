package managers;

import model.Rent;
import model.Renter;
import model.Volume;
import repositories.RentRepo;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.List;

public class RentManager {
    private final RentRepo rentRepo;


    public RentManager(RentRepo rentRepo) {
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

        if (volume.checkIfRented()) {
            throw new Exception("Volume is already rented: " + volume.getTitle());
        }

        if (renter.getRents() >= 5) {
            throw new Exception("Renter has reached the maximum number of rents: " + renter.getId());
        }


        try {
            rentRepo.bookVolume(renter, volume, rentStart);
            renter.incrementRentCount();
        } catch (RuntimeException e) {
            throw new RuntimeException("The rental process failed for volume: " + volume.getTitle(), e);
        }
    }
    public void returnVolume(UUID rentId, LocalDateTime rentEnd) {
        Rent rent = rentRepo.get(rentId);

        rent.endRent(rentEnd);

        Volume volume = rent.getVolume();
        volume.setRentedStatus(false);

        Renter renter = rent.getRenter();
        renter.decrementRentCount();

        rentRepo.returnVolume(rent, rentEnd);
    }

    // Fetch rent by its ID
    public Rent getRent(UUID rentId) {
        return rentRepo.get(rentId);
    }

    // Get all rents
    public List<Rent> getAllRents() {
        return rentRepo.getAll();
    }
}