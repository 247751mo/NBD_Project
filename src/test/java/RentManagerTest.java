import model.Book;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


import managers.RentManager;
import model.Rent;
import model.Renter;
import model.Volume;
import repositories.MongoRenterRepo;
import repositories.MongoVolumeRepo;

import java.time.LocalDateTime;

public class RentManagerTest {
    private static RentRepo rentRepo;
    private static MongoRenterRepo mongoRenterRepo;
    private static MongoVolumeRepo mongoVolumeRepo;
    private static RentManager rentManager;


    @BeforeAll
    public static void setUp() {
        rentRepo = new RentRepo();
        mongoRenterRepo = new MongoRenterRepo();
        mongoVolumeRepo = new MongoVolumeRepo();
        rentManager = new RentManager(rentRepo);
    }

    @AfterAll
    public static void tearDown() {
        rentRepo.getDatabase().getCollection("rents", Rent.class).drop();
        mongoVolumeRepo.getDatabase().getCollection("volumes", Volume.class).drop();
        mongoRenterRepo.getDatabase().getCollection("renters", Renter.class).drop();
        rentRepo.close();
        mongoRenterRepo.close();
        mongoVolumeRepo.close();
    }

    @Test
    void cantRentOneBookTwoTimes() {
        rentRepo.getDatabase().getCollection("rents", Rent.class).drop();
        rentRepo.getDatabase().getCollection("volumes", Volume.class).drop();
        rentRepo.getDatabase().getCollection("renters", Renter.class).drop();
        Book volume = new Book(2, "vol2", "Another Volume", "Fantasy");
        volume.setIsRented(1);
        Renter renter = new Renter("124", "Doe", "Jane");

        mongoVolumeRepo.create(volume);
        rentRepo.getDatabase().getCollection("renters", Renter.class).insertOne(renter);

        Rent rent = new Rent(renter, volume, LocalDateTime.now());
        Exception exception = assertThrows(Exception.class, () -> rentManager.rentVolume(renter, volume, LocalDateTime.now()));

        String expected = "Volume is already rented: " + volume.getTitle();
        String actual = exception.getMessage();

        assertTrue(actual.contains(expected));
    }

    @Test
    void cantRentMoreThanFiveBooks() {
        Book volume1 = new Book(1, "vol1", "First Book", "Fantasy");
        Book volume2 = new Book(7, "vol2", "Second Book", "Sci-Fi");
        Book volume3 = new Book(3, "vol3", "Third Book", "Drama");
        Book volume4 = new Book(4, "vol4", "Fourth Book", "History");
        Book volume5 = new Book(5, "vol5", "Fifth Book", "Mystery");
        Book volume6 = new Book(6, "vol6", "Sixth Book", "Adventure");
        Renter renter = new Renter("143443", "Smif", "Jon");

        rentRepo.getDatabase().getCollection("renters", Renter.class).insertOne(renter);
        mongoVolumeRepo.create(volume1);
        mongoVolumeRepo.create(volume2);
        mongoVolumeRepo.create(volume3);
        mongoVolumeRepo.create(volume4);
        mongoVolumeRepo.create(volume5);

        Rent testRent1 = new Rent(renter, volume1, LocalDateTime.now());
        Rent testRent2 = new Rent(renter, volume2, LocalDateTime.now());
        Rent testRent3 = new Rent(renter, volume3, LocalDateTime.now());
        Rent testRent4 = new Rent(renter, volume4, LocalDateTime.now());
        Rent testRent5 = new Rent(renter, volume5, LocalDateTime.now());

        rentRepo.create(testRent1);
        rentRepo.create(testRent2);
        rentRepo.create(testRent3);
        rentRepo.create(testRent4);
        rentRepo.create(testRent5);

        Exception exception = null;
        try {
            Rent testRent6 = new Rent(renter, volume6, LocalDateTime.now());
            rentRepo.create(testRent6);
        } catch (com.mongodb.MongoWriteException e) {
            exception = e;
            System.out.println("Exception caught: " + e.getMessage());
        }

        assertNotNull(exception, "Expected a MongoWriteException, but none was thrown.");
        assertTrue(exception instanceof com.mongodb.MongoWriteException, "Expected exception to be MongoWriteException.");
        assertTrue(exception.getMessage().contains("comparison failed"), "Exception message should indicate validation failure.");
    }
}



