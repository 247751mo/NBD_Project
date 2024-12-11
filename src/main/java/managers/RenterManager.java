package managers;

import model.Renter;
import repositories.MongoRenterRepo;

import java.io.Serializable;

public class RenterManager implements Serializable {

    private final MongoRenterRepo mongoRenterRepo;

    public RenterManager(MongoRenterRepo mongoRenterRepo) {
        if (mongoRenterRepo == null) {
            throw new IllegalArgumentException("clientRepository cannot be null");
        } else {
            this.mongoRenterRepo = mongoRenterRepo;
        }
    }

    public void registerRenter(Renter renter) {
        if (mongoRenterRepo.read(renter.getPersonalID()) != null) {
            throw new IllegalArgumentException("Client with the same ID already exists.");
        }
        mongoRenterRepo.create(renter);
    }

    public void unregisterRenter(Renter renter) {
        if (renter != null) {
            renter.setArchived(true);
            mongoRenterRepo.update(renter);
        }
    }
}