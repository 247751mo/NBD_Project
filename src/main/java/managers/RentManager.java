// Created by student on 10.06.23
package managers;
import model.Rent;
import model.Volume;
import repositories.RentRepo;
import java.util.ArrayList;
import java.util.List;

public class RentManager {

    private List<Rent> rents = new ArrayList<>();

    public boolean createRent(List<Volume> volumes, Rent rent) {
        RentRepo rep = new RentRepo();
        rep.addRent(rent, rents);
        VolumeManager s = new VolumeManager();
        List<Rent> res;
        for (int i = 0; i < volumes.size(); i++) {
            res = s.getAllVolumeRents(volumes.get(i), rents);
            if (res.size() == 0) {
                rent.setVolume(volumes.get(i));
                return true;
            }
            rent.setVolume(volumes.get(i));
        }
        return false;
    }

    public int countRents() {
        return rents.size();
    }
}
