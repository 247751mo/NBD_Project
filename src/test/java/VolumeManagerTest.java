import model.Book;
import model.Volume;
import org.junit.jupiter.api.*;
import repositories.VolumeRepo;
import java.util.List;
import managers.*;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class VolumeManagerTest {

    private VolumeRepo volumeRepo;
    private VolumeManager volumeManager;

    @BeforeAll
    void setupDatabase() {
        volumeRepo = new VolumeRepo();
        volumeRepo.initDbConnection();
        volumeManager = new VolumeManager(volumeRepo);
    }

    @AfterAll
    void closeDatabase() {
        volumeRepo.close();
    }

    @BeforeEach
    void cleanUp() {
        volumeRepo.readAll().forEach(volumeRepo::delete);
    }

    @Test
    @Order(1)
    void testAddVolume_whenVolumeDoesNotExist_shouldAddSuccessfully() {
        Book volume = new Book(1,"asda","adsadas","asdadaaa");


        volumeManager.addVolume(volume);

        Volume retrievedVolume = volumeRepo.read(1);
        assertNotNull(retrievedVolume);
        assertEquals("asda", retrievedVolume.getTitle());
        assertEquals("adsadas", retrievedVolume.getGenre());
    }

    @Test
    @Order(2)
    void testAddVolume_whenVolumeAlreadyExists_shouldThrowException() {
        Book volume1 = new Book(1,"asda","adsadas","asdadaaa");

        volumeRepo.create(volume1); // Dodajemy istniejący rekord

        Book volume2 = new Book(1,"asda2","adsadas2","asdadaaa2");

        assertThrows(IllegalArgumentException.class, () -> volumeManager.addVolume(volume2));
    }

    @Test
    @Order(3)
    void testRemoveVolume_shouldArchiveAndMarkAsNotRented() {
        Book volume1 = new Book(1,"asda","adsadas","asdadaaa");

        volumeRepo.create(volume1);

        volumeManager.removeVolume(volume1);

        assertNotNull(volume1);
        assertTrue(volume1.isArchive());
        assertEquals(0, volume1.getIsRented());
    }
}
