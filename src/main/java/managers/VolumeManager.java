package managers;
import model.Rent;
import model.Volume;
import java.util.ArrayList;
import java.util.List;

public class VolumeManager {

    private List<Volume> volumes = new ArrayList<>();

    public List<Rent> getAllVolumeRents(Volume volume, List<Rent> rents) {
        List<Rent> volumeRents = new ArrayList<>();
        for (Rent rent : rents) {
            if (rent.getVolume() == null) {
                return volumeRents;
            }
            if (rent.getVolume().getTitle().equals(volume.getTitle())) {
                volumeRents.add(rent);
            }
        }
        return volumeRents;
    }
    public void addVolume(Volume volume) {
        volumes.add(volume);
    }
    public int countVolumes() {
        return volumes.size();
    }
}
