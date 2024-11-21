import model.Book;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoClients;
import managers.RentManager;
import model.Rent;
import model.Renter;
import model.Volume;
import repositories.RentRepo;
import repositories.RenterRepo;
import repositories.VolumeRepo;

import java.time.LocalDateTime;

class RentManagerTest {
    private static RentRepo rentRepo;
    private static RenterRepo renterRepo;
    private static VolumeRepo volumeRepo;
    private static RentManager rentManager;

    @BeforeAll
    public static void setUp() {
        rentRepo = new RentRepo();
        renterRepo = new RenterRepo();
        volumeRepo = new VolumeRepo();

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
        rentRepo.getDatabase().getCollection("volumes", Volume.class).drop();
        rentRepo.getDatabase().getCollection("renters", Renter.class).drop();
        rentRepo.close();
    }
    @Test
    void cantRentOneBookTwoTimes(){
        Book volume = new Book(2,"vol2", "Another Volume", "Fantasy");
        Renter renter1 = new Renter("124", "Doe", "Jane");
        Renter renter2 = new Renter("123", "Doe", "John");
        Rent rent1 = new Rent("1", renter1, volume, LocalDateTime.now());
        Rent rent2 = new Rent("2", renter2, volume, LocalDateTime.now());
        volumeRepo.create(volume);
        renterRepo.create(renter1);
        renterRepo.create(renter2);

        // Test wypożyczenia dwóch książek
        rentRepo.create(rent1);
        rentRepo.create(rent2);



    }
}
