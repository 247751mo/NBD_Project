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
        Rent testRent1 = new Rent(renter, volume1, LocalDateTime.now());
        System.out.println("PojoCodecProvider registered"+ renter.getCurrentRentsNumber());
        Rent testRent2 = new Rent(renter, volume2, LocalDateTime.now());
        System.out.println("PojoCodecProvider registered"+ renter.getCurrentRentsNumber());
        Rent testRent3 = new Rent(renter, volume3, LocalDateTime.now());
        System.out.println("PojoCodecProvider registered"+ renter.getCurrentRentsNumber());
        Rent testRent4 = new Rent(renter, volume4, LocalDateTime.now());
        System.out.println("PojoCodecProvider registered"+ renter.getCurrentRentsNumber());
        Rent testRent5 = new Rent(renter, volume5, LocalDateTime.now());
        System.out.println("PojoCodecProvider registered"+ renter.getCurrentRentsNumber());
        Rent testRent6 = new Rent(renter, volume6, LocalDateTime.now());
        Exception exception = assertThrows(Exception.class, () -> rentManager.rentVolume(renter, volume6, LocalDateTime.now()));
        System.out.println("PojoCodecProvider registered"+ renter.getCurrentRentsNumber());
        String expected = "Renter has reached the maximum number of rents: " + renter.getPersonalID();
        String actual = exception.getMessage();

        assertTrue(actual.contains(expected));

    }
}

