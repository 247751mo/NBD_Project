package managers;

import model.Renter;
import repositories.MongoRenterRepo;

import java.io.Serializable;

public class RenterManager implements Serializable {

    private final MongoRenterRepo renterRepo;

    public RenterManager(MongoRenterRepo mongoRenterRepo) {
        if (mongoRenterRepo == null) {
            throw new IllegalArgumentException("clientRepository cannot be null");
        } else {
            this.renterRepo = mongoRenterRepo;
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