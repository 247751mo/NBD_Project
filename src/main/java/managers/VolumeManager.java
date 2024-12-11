package managers;
import model.Volume;
import repositories.MongoVolumeRepo;
import java.io.Serializable;

public class VolumeManager implements Serializable {

    private MongoVolumeRepo mongoVolumeRepo;

    public VolumeManager(MongoVolumeRepo mongoVolumeRepo) {
        if (mongoVolumeRepo == null) {
            throw new IllegalArgumentException("volumeRepo cannot be null");
        } else {
            this.mongoVolumeRepo = mongoVolumeRepo;
        }
    }

    public void addVolume(Volume volume) {
        if (volume.getVolumeId() == null) {
            mongoVolumeRepo.create(volume);
        } else if (mongoVolumeRepo.read(volume.getVolumeId()) != null) {
            throw new IllegalArgumentException("Volume already exists");
        } else {
            mongoVolumeRepo.create(volume);
        }
    }


    public void removeVolume(Volume volume) {
        if (volume != null) {
            volume.setArchive(true);
            volume.setIsRented(0);
            mongoVolumeRepo.update(volume);
        }
    }
}
