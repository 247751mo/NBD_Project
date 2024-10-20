import managers.VolumeManager;
import model.Volume;
import model.Book;
import model.Renter;
import model.Rent;
import model.NoCard;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class VolumeManagerTests {

    @Test
    void getAllVolumeRents_ReturnsEmptyList_WhenRentListIsEmpty() {
        // Arrange
        VolumeManager manager = new VolumeManager();
        Volume volume = new Volume("Ksiazka", "Fantasy");
        List<Rent> rents = new ArrayList<>();

        // Act
        List<Rent> result = manager.getAllVolumeRents(volume, rents);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void getAllVolumeRents_ReturnsMatchingRents_WhenRentListIsNotEmpty() {
        // Arrange
        VolumeManager manager = new VolumeManager();
        UUID id = UUID.randomUUID();
        LocalDateTime beginTime = LocalDateTime.now();
        Renter renter = new Renter("John", "Doe", "12345", new NoCard());
        Volume volume = new Book("Henryk Sienkiewicz", "W pustyni i w puszczy", "Powiesc");
        List<Rent> rents = new ArrayList<>();

        Rent rent1 = new Rent(id, renter, volume, beginTime);
        Rent rent2 = new Rent(id, renter, volume, beginTime);
        Rent rent3 = new Rent(id, renter, volume, beginTime);
        rents.add(rent1);
        rents.add(rent2);
        rents.add(rent3);

        // Act
        List<Rent> result = manager.getAllVolumeRents(volume, rents);

        // Assert
        assertEquals(3, result.size());
        assertEquals(rent1, result.get(0));
        assertEquals(rent2, result.get(1));
        assertEquals(rent3, result.get(2));
    }

    @Test
    void countVolumes_ReturnsCorrectCount_WhenVolumesExist() {
        // Arrange
        VolumeManager manager = new VolumeManager();
        Volume volume1 = new Volume("Volume 1", "Fantasy");
        Volume volume2 = new Volume("Volume 2", "Fantasy");


        manager.addVolume(volume1);
        manager.addVolume(volume2);
        // Act
        int count = manager.countVolumes();

        // Assert
        assertEquals(2, count);
    }

    @Test
    void countVolumes_ReturnsZero_WhenNoVolumesExist() {
        // Arrange
        VolumeManager manager = new VolumeManager();

        // Act
        int count = manager.countVolumes();

        // Assert
        assertEquals(0, count);
    }
}
