// Created by student on 10.06.23
package managers;
import model.Rent;
import model.Volume;
import repositories.RentRepo;
import java.util.ArrayList;
import java.util.List;

public class RentManager {

    private final RentRepo rentRepo; // Maintain a single instance of RentRepo
    private final List<Rent> rents; // List of rents

    public RentManager() {
        this.rentRepo = new RentRepo();
        this.rents = new ArrayList<>();
    }

    public boolean createRent(List<Volume> volumes, Rent rent) {
        for (Volume volume : volumes) {
            List<Rent> existingRents = rentRepo.getAllRents();

            // Check if the volume is already rented
            if (existingRents.stream().noneMatch(existingRent -> existingRent.getVolume().equals(volume))) {
                rent.setVolume(volume); // Set the volume if it's not rented
                rentRepo.addRent(rent, rents); // Add the rent to the RentRepo
                rents.add(rent); // Also add it to the local list
                return true;
            }
        }
        rent.setVolume(null); // Clear the volume if no valid rent could be created
        return false;
    }


    public int countRents() {
        return rents.size(); // Return the count of local rents
    }
}