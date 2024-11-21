import model.Book;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


import managers.RentManager;
import model.Rent;
import model.Renter;
import model.Volume;
import repositories.RentRepo;
import repositories.RenterRepo;
import repositories.VolumeRepo;

import java.time.LocalDateTime;

public class RentManagerTest {
    private static RentRepo rentRepo;
    private static RenterRepo renterRepo;
    private static VolumeRepo volumeRepo;
    private static RentManager rentManager;
    private Rent rentTest;

    @BeforeAll
    public static void setUp() {
        rentRepo = new RentRepo();
        renterRepo = new RenterRepo();
        volumeRepo = new VolumeRepo();
        rentManager = new RentManager(rentRepo);
    }

    @BeforeEach
    public void cleanUp() {
        rentRepo.getDatabase().getCollection("rents", Rent.class).drop();
        rentRepo.getDatabase().getCollection("volumes", Volume.class).drop();
        rentRepo.getDatabase().getCollection("renters", Renter.class).drop();
    }

    @AfterAll
    public static void tearDown() {
        rentRepo.getDatabase().getCollection("rents", Rent.class).drop();
        volumeRepo.getDatabase().getCollection("volumes", Volume.class).drop();
        renterRepo.getDatabase().getCollection("renters", Renter.class).drop();
        rentRepo.close();
        renterRepo.close();
        volumeRepo.close();
    }

    @Test
    void cantRentOneBookTwoTimes() {
        Book volume = new Book(2, "vol2", "Another Volume", "Fantasy");
        Renter renter = new Renter("124", "Doe", "Jane");

        volumeRepo.create(volume);
        renterRepo.create(renter);
        volume.setIsRented(1); // Volume is already rented

        Exception exception = assertThrows(Exception.class, () -> rentManager.rentVolume(renter, volume, LocalDateTime.now()));

        String expected = "Volume is already rented: " + volume.getTitle();
        String actual = exception.getMessage();

        assertTrue(actual.contains(expected));
    }

    @Test
    void cantRentMoreThanFiveBooks() {
        Book volume1 = new Book(1, "vol1", "First Book", "Fantasy");
        Book volume2 = new Book(2, "vol2", "Second Book", "Sci-Fi");
        Book volume3 = new Book(3, "vol3", "Third Book", "Drama");
        Book volume4 = new Book(4, "vol4", "Fourth Book", "History");
        Book volume5 = new Book(5, "vol5", "Fifth Book", "Mystery");
        Book volume6 = new Book(6, "vol6", "Sixth Book", "Adventure");
        Renter renter = new Renter("124", "Doe", "Jane");


        volumeRepo.create(volume1);
        volumeRepo.create(volume2);
        volumeRepo.create(volume3);
        volumeRepo.create(volume4);
        volumeRepo.create(volume5);
        volumeRepo.create(volume6);
        renterRepo.create(renter);
        Rent testRent1 = new Rent(renter, volume1,LocalDateTime.now());
        Rent testRent2 = new Rent(renter, volume2,LocalDateTime.now());
        Rent testRent3 = new Rent(renter, volume3,LocalDateTime.now());
        Rent testRent4 = new Rent(renter, volume4,LocalDateTime.now());
        Rent testRent5 = new Rent(renter, volume5,LocalDateTime.now());
        Exception exception = assertThrows(Exception.class, () -> rentManager.rentVolume(renter, volume6, LocalDateTime.now()));

        String expected = "Renter has reached the maximum number of rents: " + renter.getPersonalID();
        String actual = exception.getMessage();

        assertTrue(actual.contains(expected));

    }

    @Test
    void returnVolumeCorrectly() {
        Book volume = new Book(1, "vol1", "Book to Return", "Fantasy");
        Renter renter = new Renter("124", "Doe", "Jane");

        volumeRepo.create(volume);
        renterRepo.create(renter);

        rentManager.rentVolume(renter, volume, LocalDateTime.now());

        // Return the volume
        rentManager.returnVolume("1", LocalDateTime.now());

        Rent rent = rentManager.getRent("1");

        assertTrue(rent.getEndRent() != null);  // Ensure that the rent end date is set
        assertFalse(volume.getIsRented() == 1); // Ensure the volume is no longer rented
    }

    @Test
    void getRentById() {
        Book volume = new Book(1, "vol1", "Book", "Fantasy");
        Renter renter = new Renter("124", "Doe", "Jane");

        volumeRepo.create(volume);
        renterRepo.create(renter);

        rentManager.rentVolume(renter, volume, LocalDateTime.now());

        Rent rent = rentManager.getRent("1");

        assertNotNull(rent);
        assertEquals("1", rent.getRentId());  // Ensure the rent ID is correct
        assertEquals(volume.getTitle(), rent.getVolume().getTitle());  // Ensure the volume is correct
    }

    @Test
    void getAllRents() {
        Book volume1 = new Book(1, "vol1", "Book 1", "Fantasy");
        Book volume2 = new Book(2, "vol2", "Book 2", "Sci-Fi");
        Renter renter = new Renter("124", "Doe", "Jane");

        volumeRepo.create(volume1);
        volumeRepo.create(volume2);
        renterRepo.create(renter);

        rentManager.rentVolume(renter, volume1, LocalDateTime.now());
        rentManager.rentVolume(renter, volume2, LocalDateTime.now());

        List<Rent> rents = rentManager.getAllRents();

        assertNotNull(rents);
        assertEquals(2, rents.size());  // There should be two rents
    }
}