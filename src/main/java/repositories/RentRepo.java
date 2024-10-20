package repositories;

import model.Rent;
import java.util.ArrayList;
import java.util.List;

public class RentRepo {


    private final List<Rent> rents;


    public RentRepo() {
        this.rents = new ArrayList<>();
    }


    public void addRent(Rent rent) {
        rents.add(rent);
    }


    public void removeRent(Rent rent) {
        rents.removeIf(existingRent -> existingRent.equals(rent));
    }


    public List<Rent> getAllRents() {
        return new ArrayList<>(rents);
    }
}