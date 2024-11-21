import com.mongodb.client.model.Filters;
import model.Book;
import model.Rent;
import model.Renter;
import model.Volume;
import org.junit.jupiter.api.*;
import repositories.RentRepo;
import repositories.RenterRepo;
import repositories.VolumeRepo;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class RentRepoTest {

    private static RentRepo rentRepo;
    private static RenterRepo renterRepo;
    private static VolumeRepo volumeRepo;

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
    void testCreateRent() {
        Renter renter = new Renter("123", "Doe", "John");
        Book volume = new Book(1,"vol1", "Test Volume", "Science Fiction");
        Rent rent = new Rent(renter, volume, LocalDateTime.now());

        rentRepo.getDatabase().getCollection("renters", Renter.class).insertOne(renter);
        rentRepo.getDatabase().getCollection("volumes", Volume.class).insertOne(volume);

        rentRepo.create(rent);
        System.out.println("PojoCodecProvider registered"+ renter.getCurrentRentsNumber());
        Rent foundRent = rentRepo.read(rent.getId());
        assertNotNull(foundRent);
        assertEquals(renter.getPersonalID(), foundRent.getRenter().getPersonalID());
        assertEquals(volume.getVolumeId(), foundRent.getVolume().getVolumeId());
    }

    @Test
    void testDeleteRent() {
        Renter renter = new Renter("124", "Doe", "Jane");
        Book volume = new Book(2,"vol2", "Another Volume", "Fantasy");
        Rent rent = new Rent(renter, volume, LocalDateTime.now());

        rentRepo.getDatabase().getCollection("renters", Renter.class).insertOne(renter);
        rentRepo.getDatabase().getCollection("volumes", Volume.class).insertOne(volume);

        rentRepo.create(rent);
        Rent foundRent = rentRepo.read(rent.getId());
        assertNotNull(foundRent);

        rentRepo.delete(rent);
        Rent deletedRent = rentRepo.read(rent.getId());
        assertNull(deletedRent);
    }

    @Test
    void testUpdateRent() {
        Renter renter = new Renter("125", "Doe", "Max");
        Book volume = new Book(3,"vol3", "Volume to Update", "Thriller");
        Rent rent = new Rent(renter, volume, LocalDateTime.now());

        rentRepo.getDatabase().getCollection("renters", Renter.class).insertOne(renter);
        rentRepo.getDatabase().getCollection("volumes", Volume.class).insertOne(volume);

        rentRepo.create(rent);

        rent.setEndTime(LocalDateTime.now().plusDays(1));
        rentRepo.update(rent);

        Rent updatedRent = rentRepo.read(rent.getId());
        assertNotNull(updatedRent.getEndTime());
        assertEquals(rent.getEndTime().truncatedTo(ChronoUnit.SECONDS), updatedRent.getEndTime().truncatedTo(ChronoUnit.SECONDS));

    }

    @Test
    void testReadAllRents() {
        List<Rent> rents = rentRepo.readAll();
        int initialSize = rents.size();

        Renter renter1 = new Renter("126", "Smith", "Alice");
        Renter renter2 = new Renter("127", "Brown", "Bob");
        Book volume1 = new Book(4,"vol4", "First Volume", "Drama");
        Book volume2 = new Book(5,"vol5", "Second Volume", "Horror");

        rentRepo.getDatabase().getCollection("renters", Renter.class).insertOne(renter1);
        rentRepo.getDatabase().getCollection("renters", Renter.class).insertOne(renter2);
        rentRepo.getDatabase().getCollection("volumes", Volume.class).insertOne(volume1);
        rentRepo.getDatabase().getCollection("volumes", Volume.class).insertOne(volume2);

        rentRepo.create(new Rent(renter1, volume1, LocalDateTime.now()));
        rentRepo.create(new Rent(renter2, volume2, LocalDateTime.now()));

        rents = rentRepo.readAll();
        assertEquals(initialSize + 2, rents.size());
    }
}
