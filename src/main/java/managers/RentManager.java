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

        // Check if the volume is available for rent
        if (volume.checkIfRented()) {
            throw new Exception("Volume is already rented: " + volume.getTitle());
        }

        // Check if the renter has reached the maximum number of rents
        if (renter.getRents() >= 5) { // Assuming getRents() returns a collection
            throw new Exception("Renter has reached the maximum number of rents: " + renter.getId());
        }

        // Proceed with the booking
        try {
            rentRepo.bookVolume(renter, volume, rentStart);
        } catch (RuntimeException e) {
            throw new RuntimeException("The rental process failed for volume: " + volume.getTitle(), e);
        }
    }
    public void returnVolume(UUID rentId, LocalDateTime rentEnd) {
        // Fetch the rent by its ID
        Rent rent = rentRepo.get(rentId);

        // End the rent and mark the volume as returned
        rent.endRent(rentEnd);

        // Return the volume in the repository
        rentRepo.returnVolume(rent, rentEnd);

        // Mark the rent as archived (this could mean it's no longer active and stored for history purposes)
        rent.setVolume(null); // Assuming nulling volume means archiving it
        rentRepo.update(rent);
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