package managers;

import model.Renter;
import repositories.RenterRepo;

import java.io.Serializable;

public class RenterManager implements Serializable {

    private final RenterRepo renterRepo;

    public RenterManager(RenterRepo renterRepo) {
        if (renterRepo == null) {
            throw new IllegalArgumentException("clientRepository cannot be null");
        } else {
            this.renterRepo = renterRepo;
        }
    }

    public void registerRenter(Renter renter) {
        if (renterRepo.read(renter.getPersonalID()) != null) {
            throw new IllegalArgumentException("Client with the same ID already exists.");
        }
        renterRepo.create(renter);
    }

    public void unregisterRenter(Renter renter) {
        if (renter != null) {
            renter.setArchived(true);
            renterRepo.update(renter);
        }
    }
}