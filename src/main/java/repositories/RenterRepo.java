package repositories;

import model.Renter;

import java.util.ArrayList;
import java.util.List;

public class RenterRepo {
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