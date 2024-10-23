package managers;
import model.Renter;
import model.Volume;
import repositories.RenterRepo;
import java.io.Serializable;

public class RenterManager implements Serializable {

    private RenterRepo renterRepo;

    public RenterManager(RenterRepo renterRepo) {
        if (renterRepo == null) {
            throw new IllegalArgumentException("renterRepo cannot be null");
        } else {
            this.renterRepo = renterRepo;
        }
    }

    public void addRenter(Renter renter) {
        if (renterRepo.get(renter.getId()) != null) {
            throw new IllegalArgumentException("Volume already exists");
        } else {
            renterRepo.add(renter);
        }
    }

    public void removeRenter(Renter renter) {
        if (renter != null) {
            renter.setArchiveStatus(true);
            renterRepo.update(renter);
        }
    }
}
