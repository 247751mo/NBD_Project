import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import model.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import repositories.RenterRepo;
import repositories.RentRepo;
import repositories.VolumeRepo;
import model.Rent;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class RentRepoTest {
    private static RentRepo rentRepo;
    private static EntityManagerFactory entityManagerFactory;
    private static EntityManager entityManager;
    private static VolumeRepo volumeRepo;
    private static RenterRepo renterRepo;
    private static EntityManager em;

    @BeforeAll
    public static void setUp() {
        entityManagerFactory = Persistence.createEntityManagerFactory("default");
        entityManager = entityManagerFactory.createEntityManager();
        rentRepo = new RentRepo(entityManager);
        renterRepo = new RenterRepo(entityManager);
        volumeRepo = new VolumeRepo(entityManager);
        em = entityManagerFactory.createEntityManager();
    }
    @Test
    void testAddRent() {
        Renter renter = new Renter("Frank", "Ocean", "FRANK325", new NoCard());
        Book book = new Book("Andrzej Sapkowski", "Wiedzmin Krew Elfow", "Fantasy");

        renterRepo.add(renter);
        volumeRepo.add(book);

        Rent rent = new Rent(renter, book, LocalDateTime.now());
        rentRepo.add(rent);
        Rent foundRent = rentRepo.get(rent.getId());

        assertEquals(rent, foundRent);
    }

}