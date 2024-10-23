//import model.Volume;
//import org.junit.jupiter.api.Test;
//import static org.junit.jupiter.api.Assertions.*;
//
//class VolumeTest {
//
//    @Test
//    void constructorAndGetters() {
//        // Arrange
//        String title = "ksiazkezka";
//        String genre = "fantasy";
//        Volume volume = new Volume(title, genre);
//
//        // Act & Assert
//        assertEquals(title, volume.getTitle());
//        assertEquals(genre, volume.getGenre());
//        // Uncomment if the methods checkIfRented and checkIfArchived exist
//        assertFalse(volume.checkIfRented());
//        assertFalse(volume.checkIfArchived());
//    }
//
//    @Test
//    void setters() {
//        // Arrange
//        String title = "ksiazka";
//        String genre = "fantasy";
//        Volume volume = new Volume(title, genre);
//
//        String newTitle = "nowaksiazka";
//        String newGenre = "dokument";
//        boolean isRented = true;
//        boolean isArchived = true;
//
//        // Act
//        volume.setTitle(newTitle);
//        volume.setGenre(newGenre);
//        volume.setRentedStatus(isRented);
//        volume.setArchiveStatus(isArchived);
//
//        // Assert
//        assertEquals(newTitle, volume.getTitle());
//        assertEquals(newGenre, volume.getGenre());
//        assertEquals(isRented, volume.checkIfRented());
//        assertEquals(isArchived, volume.checkIfArchived());
//    }
//
//    @Test
//    void volumeInfo() {
//        // Arrange
//        String title = "ksiazka";
//        String genre = "fantasy";
//        Volume volume = new Volume(title, genre);
//
//        String expectedInfo = "Title: ksiazka\nGenre: fantasy";
//
//        // Act & Assert
//        assertEquals(expectedInfo, volume.volumeInfo());
//    }
//}