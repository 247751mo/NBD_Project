package repositories;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import model.Renter;
import model.Volume;
import java.util.ArrayList;
import java.util.List;

public class VolumeRepo implements Repo<Renter>{

    private final List<Volume> volumes;


    public VolumeRepo() {
        this.volumes = new ArrayList<>();
    }


    public void addVolume(Volume volume, List<Volume> volumes) {
        volumes.add(volume);
    }


    public void removeVolume(Volume volume, List<Volume> volumes) {
        volumes.remove(volume);
    }

    public List<Volume> getAllVolumes() {
        return new ArrayList<>(volumes);
    }
}
