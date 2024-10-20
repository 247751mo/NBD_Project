import model.Volume;
import org.junit.jupiter.api.Test;
import repositories.VolumeRepo;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class VolumeRepoTest {

    @Test
    void addVolumeTest() {
        // Arrange
        VolumeRepo repo = new VolumeRepo();
        List<Volume> volumes = new ArrayList<>();

        Volume volume = new Volume("title", "genre");

        // Act
        repo.addVolume(volume, volumes);

        // Assert
        assertEquals(1, volumes.size());
        assertEquals(volume, volumes.getFirst());
    }

    @Test
    void removeVolumeTest() {
        // Arrange
        VolumeRepo repo = new VolumeRepo();
        List<Volume> volumes = new ArrayList<>();

        Volume volume = new Volume("title", "genre");
        repo.addVolume(volume, volumes);

        // Act
        repo.removeVolume(volume, volumes);

        // Assert
        assertEquals(0, volumes.size());
        assertFalse(volumes.contains(volume));
    }

    @Test
    void removeNonExistentVolumeTest() {
        // Arrange
        VolumeRepo repo = new VolumeRepo();
        List<Volume> volumes = new ArrayList<>();

        Volume volume = new Volume("title", "genre");

        // Act
        repo.removeVolume(volume, volumes);

        // Assert
        assertEquals(0, volumes.size());
    }
}
