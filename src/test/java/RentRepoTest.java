/*import jakarta.persistence.EntityManager;
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
import java.time.temporal.ChronoUnit;
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

        renterRepo.create(renter);
        volumeRepo.create(book);

        Rent rent = new Rent(renter, book, LocalDateTime.now());
        rentRepo.create(rent);
        Rent foundRent = rentRepo.read(rent.getId());

        assertEquals(rent, foundRent);
    }
    @Test
    void testRemoveRent() {
        Renter renter = new Renter("Tyler", "Okonma", "TYLER1", new NoCard());
        Book book = new Book("Homer", "Odyseja", "Epos");

        renterRepo.create(renter);
        volumeRepo.create(book);

        Rent rent = new Rent(renter, book, LocalDateTime.now());
        rentRepo.create(rent);

        Rent foundRent1 = rentRepo.read(rent.getId());
        assertNotNull(foundRent1);

        rentRepo.delete(rent);

        Rent foundRent2 = rentRepo.read(rent.getId());
        assertNull(foundRent2);
    }
    @Test
    void testUpdateRent() {
        Renter renter = new Renter("Kali", "Uchis", "KALI343", new NoCard());
        Book book = new Book("Boleslaw Prus", "Kamizelka", "Opowiadanie");
        Rent rent = new Rent(renter, book, LocalDateTime.now());

        em.getTransaction().begin();
        em.persist(renter);
        em.persist(book);
        em.getTransaction().commit();

        rentRepo.create(rent);
        UUID rentId = rent.getId();
        assertNotNull(rentId,"The rent ID should not be null after persisting");
        Rent retrievedRent = rentRepo.read(rentId);
        assertNotNull(retrievedRent, "The retrieved rent should not be null");
        assertEquals(rentId, retrievedRent.getId(), "The retrieved rent ID should match the persisted rent ID");
    }
    @Test
    void testGetAllRents() {


        List<Rent> rents = rentRepo.readAll();
        int initialSize = rents.size();

        Renter renter1 = new Renter("Emma", "Davis", "EMMA123", new NoCard());
        Renter renter2 = new Renter("Frank", "Taylor", "FRANK456", new Card());
        Book book1 = new Book("Boleslaw Prus", "Lalka", "Powiesc");
        Book book2 = new Book("Sofokles", "Krol Edyp", "Tragedia");

        entityManager.getTransaction().begin();
        entityManager.persist(renter1);
        entityManager.persist(renter2);
        entityManager.persist(book1);
        entityManager.persist(book2);
        entityManager.getTransaction().commit();

        Rent rent1 = new Rent(renter1, book1, LocalDateTime.now());
        Rent rent2 = new Rent(renter2, book2, LocalDateTime.now());

        rentRepo.create(rent1);
        rentRepo.create(rent2);

        rents = rentRepo.readAll();
        int finalSize = rents.size();

        assertEquals(initialSize + 2, finalSize);
    }
    @Test
    void testBookVolume() {
        entityManager.getTransaction().begin();

        Renter renter = new Renter("George", "Hall", "GEORGE789", new NoCard());
        Book book = new Book("Adam Mickiewicz", "Pan Tadeusz", "Epika");
        entityManager.persist(renter);
        entityManager.persist(book);

        entityManager.getTransaction().commit();

        rentRepo.bookVolume(renter, book, LocalDateTime.now());

        Volume foundBook = entityManager.find(Volume.class, book.getVolumeId());
        assertTrue(foundBook.checkIfRented());
    }
}*/