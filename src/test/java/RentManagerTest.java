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
    void cantRentOneBookTwoTimes(){
        Book volume = new Book(2,"vol2", "Another Volume", "Fantasy");
        Renter renter = new Renter("124", "Doe", "Jane");


        volumeRepo.create(volume);
        renterRepo.create(renter);
        volume.setIsRented(1);

        Exception exception = assertThrows(Exception.class, () -> rentManager.rentVolume(renter,volume,LocalDateTime.now()));

        String expected= "Volume is already rented: " + volume.getTitle();
        String actual = exception.getMessage();

        assertTrue(actual.contains(expected));
    }
}
