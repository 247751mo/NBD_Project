import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import managers.VolumeManager;
import model.Book;
import model.Volume;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import repositories.VolumeRepo;

import static org.junit.jupiter.api.Assertions.*;

public class VolumeManagerTest {

    private static VolumeRepo volumeRepo;
    private static VolumeManager volumeManager;

    @BeforeAll
    public static void setUp() {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        volumeRepo = new VolumeRepo(entityManager);
        volumeManager = new VolumeManager(volumeRepo);
    }

    @Test
    void testAddVolumeWithNullRepository() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new VolumeManager(null);
        });

        assertEquals("volumeRepo cannot be null", exception.getMessage());
    }

    @Test
    void testAddVolume() {
        Book book = new Book("Stanislaw Lem", "Bajki robotow", "Science Fiction");
        volumeManager.addVolume(book);
        Volume addedVolume = volumeRepo.get(book.getVolumeId());
        assertEquals(book, addedVolume);
    }

    @Test
    void testAddedBookWithExistingId() {
        Book book = new Book("Stanislaw Lem", "Niezwyciezony", "Science Fiction");
        volumeRepo.add(book);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            volumeManager.addVolume(book);
        });

        assertEquals("Volume already exists", exception.getMessage());
    }

    @Test
    void testRemoveBook() {
        Book book = new Book("Frank Herbert", "Diuna", "Science Fiction");
        volumeRepo.add(book);

        volumeManager.removeVolume(book);

        Volume removedBook = volumeRepo.get(book.getVolumeId());
        assertTrue(removedBook.checkIfArchived());
        assertFalse(removedBook.checkIfRented());
    }
}