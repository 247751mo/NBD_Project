import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import model.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import repositories.VolumeRepo;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class VolumeRepoTest {

    private static VolumeRepo volumeRepo;

    @BeforeAll
    public static void setUp() {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        volumeRepo = new VolumeRepo(entityManager);
    }
    @Test
    void testAddVolume() {
        Book book = new Book("Henryk Sienkiewicz", "Potop", "Historyczne");
        Monthly monthly = new Monthly("Top Gear", "Automotive", "Immediate Media Company");

        volumeRepo.add(book);
        volumeRepo.add(monthly);

        Book foundVolume1 = (Book) volumeRepo.get(book.getVolumeId());
        Monthly foundVolume2 = (Monthly) volumeRepo.get(monthly.getVolumeId());

        assertEquals(book, foundVolume1);
        assertEquals(monthly, foundVolume2);
    }
    @Test
    void testRemoveVolume() {
        Book book = new Book("Adam Mickiewicz", "Pan Tadeusz", "Epic Poetry");
        volumeRepo.add(book);

        Book foundBook1 = (Book) volumeRepo.get(book.getVolumeId());
        assertNotNull(foundBook1);

        volumeRepo.delete(book);

        Book foundBook2 = (Book) volumeRepo.get(book.getVolumeId());
        assertNull(foundBook2);
    }

}