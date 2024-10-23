package repositories;

import model.Renter;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.ArrayList;
import java.util.List;

public class RenterRepo implements Repo<Renter> {
    private final List<Renter> renters;


    public RenterRepo() {
        this.renters = new ArrayList<>();
    }


    public void addRenter(Renter renter) {
        renters.add(renter);
    }


    public void removeRenter(Renter renter) {
        renters.removeIf(existingRenter -> existingRenter.equals(renter));
    }


    public List<Renter> getAll() {
        return new ArrayList<>(renters);
    }
}