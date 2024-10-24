import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import managers.RentManager;
import managers.RenterManager;
import managers.VolumeManager;
import model.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import repositories.RentRepo;
import repositories.RenterRepo;
import repositories.VolumeRepo;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class RentManagerTest {
    private static RentRepo rentRepo;
    private static RentManager rentManager;
    private static RenterRepo renterRepo;
    private static RenterManager renterManager;
    private static VolumeRepo volumeRepo;
    private static VolumeManager volumeManager;
    private static EntityManager em;
    private static EntityManager em1;

    @BeforeAll
    public static void setUp() {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        em = entityManagerFactory.createEntityManager();
        em1 = entityManagerFactory.createEntityManager();

        rentRepo = new RentRepo(em);
        rentManager = new RentManager(rentRepo);

        renterRepo = new RenterRepo(em);
        renterManager = new RenterManager(renterRepo);

        volumeRepo = new VolumeRepo(em);
        volumeManager = new VolumeManager(volumeRepo);
    }

    @Test
    void testRentManagerWithNullRepository() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new RentManager(null);
        });
        assertEquals("rentRepo cannot be null", exception.getMessage());
    }

    @Test
    void testRentVolumeWithUnavailableVolume() throws Exception {

        RenterType cardType = new Card();

        Renter renter = new Renter("Tyler", "Okonma", "1234567890", cardType);
        Book book = new Book("Stanislaw Lem", "Bajki robotow", "Science Fiction");
        renterRepo.add(renter);
        volumeRepo.add(book);

        Exception exception = assertThrows(Exception.class, () -> {
            rentManager.rentVolume(renter, book, LocalDateTime.now());
        });

        String expectedMessage = "Volume is already rented: " + book.getTitle();
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage), "Unexpected exception message: " + actualMessage);
    }

    @Test
    void testRentVolumeWithTooManyRents() throws Exception {
        RenterType noCardType = new NoCard();

        Renter renter = new Renter("Tyler", "Okonma", "1234567890", noCardType);
        renterRepo.add(renter);


        Weekly weekly = new Weekly("tytul", "gatunek", "publikowal");
        volumeRepo.add(weekly);
        rentRepo.bookVolume(renter, weekly, LocalDateTime.now());
        Weekly weekly2 = new Weekly("tytul2", "gatunek2", "publikowal2");
        volumeRepo.add(weekly);
        rentRepo.bookVolume(renter, weekly2, LocalDateTime.now());
        Weekly weekly3 = new Weekly("tytul3", "gatunek3", "publikowal3");
        volumeRepo.add(weekly3);
        rentRepo.bookVolume(renter, weekly3, LocalDateTime.now());
        Weekly weekly4 = new Weekly("tytul4", "gatunek4", "publikowal4");
        volumeRepo.add(weekly4);
        rentRepo.bookVolume(renter, weekly4, LocalDateTime.now());
        Weekly weekly5 = new Weekly("tytul5", "gatunek5", "publikowal5");
        volumeRepo.add(weekly5);
        rentRepo.bookVolume(renter, weekly5, LocalDateTime.now());

        Weekly weekly6 = new Weekly("Fakt", "Wiadomosci", "J. Billig");
        volumeRepo.add(weekly6);

        Exception exception = assertThrows(Exception.class, () -> {
            rentManager.rentVolume(renter, weekly6, LocalDateTime.now());
        });

        String expectedMessage = "Renter has reached the maximum number of rents: " + renter.getId();
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage), "Unexpected exception message: " + actualMessage);
    }

    @Test
    void testReturnVolume() throws Exception {

        RenterType cardType = new Card();


        Renter renter = new Renter("Frank", "Ocean", "0987654321",cardType);
        Book book = new Book("Henryk Sienkiewicz", "Potop", "Historyczne");

        renterRepo.add(renter);
        volumeRepo.add(book);
        rentManager.rentVolume(renter, book, LocalDateTime.now());

        assertEquals(1, renter.getRents());

        Rent rent = rentRepo.getAll().get(0);
        rentManager.returnVolume(rent.getId(), LocalDateTime.now().plusDays(1));

        assertEquals(0, renter.getRents());
        assertFalse(book.checkIfRented());
    }

    @Test
    void testReturnVolumeWithNullRentEnd() {

        RenterType cardType = new Card();

        Renter renter = new Renter("Earl", "Sweatshirt", "0123456789",cardType);
        Monthly monthly12 = new Monthly("Top Gear", "Motoryzacyjne", "Immediate Media Company");

        renterRepo.add(renter);
        volumeRepo.add(monthly12);

        try {
            rentManager.rentVolume(renter, monthly12, LocalDateTime.now());
        } catch (Exception e) {
            System.out.println("Error while renting volume: " + e.getMessage());
        }
        Rent rent = rentRepo.getAll().get(0);

        rentManager.returnVolume(rent.getId(), null);

        assertFalse(monthly12.checkIfRented());
        assertNotNull(rent.getEndTime());
    }

    @Test
    void testGetRent() {

        RenterType noCardType = new NoCard();


        Renter renter = new Renter("Steve", "Lacy", "5678901234",noCardType);
        Book book = new Book("Frank Herbert", "Diuna", "Science Fiction");

        renterRepo.add(renter);
        volumeRepo.add(book);
        try {
            rentManager.rentVolume(renter, book, LocalDateTime.now());
        } catch (Exception e) {
            System.out.println("Error while renting volume: " + e.getMessage());
        }

        Rent rent = rentRepo.getAll().get(0);
        Rent foundRent = rentManager.getRent(rent.getId());

        assertNotNull(foundRent);
        assertEquals(rent.getId(), foundRent.getId());
    }

    @Test
    void testGetAllRents() {

        RenterType noCardType = new NoCard();
        RenterType cardType = new Card();

        Renter renter1 = new Renter("Tyler", "Okonma", "1234567890",cardType);
        Renter renter2 = new Renter("Frank", "Ocean", "0987654321",noCardType);

        Book book1 = new Book("Sofokles", "Krol Edyp", "Tragedia");
        Book book2 = new Book("Sofokles", "Krol Edyp", "Tragedia");

        renterRepo.add(renter1);
        renterRepo.add(renter2);

        volumeRepo.add(book1);
        volumeRepo.add(book2);

        try {
            rentManager.rentVolume(renter1, book1, LocalDateTime.now());
        } catch (Exception e) {
            System.out.println("Error while renting volume: " + e.getMessage());
        }
        try {
            rentManager.rentVolume(renter2, book2, LocalDateTime.now());
        } catch (Exception e) {
            System.out.println("Error while renting volume: " + e.getMessage());
        }

        assertEquals(2, rentManager.getAllRents().size());
    }
}