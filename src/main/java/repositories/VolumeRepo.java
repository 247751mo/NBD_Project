package repositories;

import model.Volume;
import java.util.ArrayList;
import java.util.List;

public class VolumeRepo {


    private final List<Volume> volumes;


    public VolumeRepo() {
        this.volumes = new ArrayList<>();
    }


    public void addVolume(Volume volume) {
        volumes.add(volume);
    }


    public void removeVolume(Volume volume) {
        volumes.removeIf(existingVolume -> existingVolume.equals(volume));
    }


    public List<Volume> getAllVolumes() {
        return new ArrayList<>(volumes);
    }
}
