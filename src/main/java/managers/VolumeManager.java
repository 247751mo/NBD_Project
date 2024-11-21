package managers;
import model.Volume;
import repositories.VolumeRepo;
import java.io.Serializable;

public class VolumeManager implements Serializable {

    private VolumeRepo volumeRepo;

    public VolumeManager(VolumeRepo volumeRepo) {
        if (volumeRepo == null) {
            throw new IllegalArgumentException("volumeRepo cannot be null");
        } else {
            this.volumeRepo = volumeRepo;
        }
    }

    public void addVolume(Volume volume) {
        if (volume.getVolumeId() == null) {
            volumeRepo.create(volume);
        } else if (volumeRepo.read(volume.getVolumeId()) != null) {
            throw new IllegalArgumentException("Volume already exists");
        } else {
            volumeRepo.create(volume);
        }
    }


    public void removeVolume(Volume volume) {
        if (volume != null) {
            volume.setArchive(true);
            volume.setIsRented(0);
            volumeRepo.update(volume);
        }
    }
}
