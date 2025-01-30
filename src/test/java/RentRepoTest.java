import com.fasterxml.jackson.core.JsonProcessingException;
import model.*;
import org.bson.Document;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import repositories.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.ExecutionException;

public class RentRepoTest {
    private static RentRepo rentRepo;
    private static VolumeRepo volumeRepo;
    private static RenterRepo renterRepo;
    private KafkaProducent kafkaProducent;

    private Volume testVolume;
    private Renter testRenter;
    private Rent testRent;

    @BeforeEach
    void setup() throws ExecutionException, InterruptedException {
        kafkaProducent = new KafkaProducent();

        rentRepo = new RentRepo();
        rentRepo.initDbConnection();

        volumeRepo = new VolumeRepo();
        volumeRepo.initDbConnection();

        renterRepo = new RenterRepo();
        renterRepo.initDbConnection();

        // Insert sample Renter
        Renter testRenter = new Renter("123356", "John", "Doe");

        // Insert sample Volume
        Volume testVolume = new Book(12, "Solaris", "Sci-fi", "Stanislaw Lem");
    }

    @AfterEach
    public void tearDown() {
        rentRepo.getDatabase().getCollection("rents", Rent.class).deleteMany(new Document());
        rentRepo.getDatabase().getCollection("archived", Rent.class).deleteMany(new Document());
        rentRepo.close();
        renterRepo.getDatabase().getCollection("renters", Renter.class).deleteMany(new Document());
        renterRepo.close();
        volumeRepo.getDatabase().getCollection("volumes", Volume.class).deleteMany(new Document());
        volumeRepo.close();
    }

    @Test
    @DisplayName("Test adding a new Rent")
    void testAddRent() {
        testRent = new Rent(testRenter, testVolume, LocalDateTime.now());

        // Add rent
        rentRepo.create(testRent);

        // Retrieve rent by ID
        Rent retrievedRent = rentRepo.read(testRent.getId());

        assertNotNull(retrievedRent);
        assertEquals(testRent.getRenter().getPersonalID(), retrievedRent.getRenter().getPersonalID());
        assertEquals(testRent.getVolume().getVolumeId(), retrievedRent.getVolume().getVolumeId());
    }

    @Test
    @DisplayName("Test deleting a Rent")
    void testEndRent() {
        testAddRent(); // Ensure we have a rent added

        // Delete rent
        rentRepo.delete(testRent);

        // Verify rent is deleted
        Rent deletedRent = rentRepo.read(testRent.getId());
        assertNull(deletedRent);

        // Verify Renter rental count decreased
        Renter updatedRenter = renterRepo.read(testRenter.getPersonalID());
        assertEquals(0, updatedRenter.getCurrentRentsNumber());
    }

    @Test
    @DisplayName("Test updating a Rent")
    public void testUpdateRent() {
        Rent rent = new Rent(testRenter, testVolume, LocalDateTime.now());
        LocalDateTime endTime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).plusHours(10);
        rentRepo.create(rent);
        rent.setEndTime(endTime);
        rentRepo.update(rent);
        assertEquals(endTime, rentRepo.read(rent.getId()).getEndTime());
    }

    @Test
    @DisplayName("Test renting the same Volume twice")
    void testRentSameVolumeTwice() {
        rentRepo.create(new Rent(testRenter, testVolume, LocalDateTime.now()));
        Rent duplicateRent = new Rent(testRenter, testVolume, LocalDateTime.now());
        assertThrows(Exception.class, () -> rentRepo.create(duplicateRent));
    }

    @Test
    @DisplayName("Test maximum number of Rents for a Renter")
    void testRenterMaxRents() {
        Volume volume1 = new Book(13, "Dune", "Sci-fi", "Frank Herbert");
        Volume volume2 = new Book(14, "Neuromancer", "Cyberpunk", "William Gibson");
        Volume volume3 = new Book(15, "Foundation", "Sci-fi", "Isaac Asimov");

        rentRepo.create(new Rent(testRenter, testVolume, LocalDateTime.now()));
        rentRepo.create(new Rent(testRenter, volume1, LocalDateTime.now()));
        assertThrows(Exception.class, () -> rentRepo.create(new Rent(testRenter, volume2, LocalDateTime.now())));
    }

    @Test
    @DisplayName("Test adding a Rent with Kafka but without MongoDB")
    void testAddRentNoMongo() throws InterruptedException, JsonProcessingException {
        KafkaConsument consument = new KafkaConsument(2);
        consument.initConsumers();
        consument.consumeTopicByAllConsumers();

        testRent = new Rent(testRenter, testVolume, LocalDateTime.now());

        kafkaProducent.sendRentAsync(testRent);

        Thread.sleep(500); // Wait for Kafka processing

        Rent retrievedRent = rentRepo.read(testRent.getId());

        assertNotNull(retrievedRent);
        assertEquals(testRent.getRenter().getPersonalID(), retrievedRent.getRenter().getPersonalID());
        assertEquals(testRent.getVolume().getVolumeId(), retrievedRent.getVolume().getVolumeId());
    }

    @Test
    @DisplayName("Test adding a Rent with Kafka and MongoDB")
    void testAddRentWithKafkaAndMongo() throws InterruptedException, JsonProcessingException {
        Rent testRent = new Rent(testRenter, testVolume, LocalDateTime.now());
        kafkaProducent.sendRentAsync(testRent);

        Rent retrievedRent = null;
        int retryCount = 0;
        while (retrievedRent == null && retryCount < 10) {
            retrievedRent = rentRepo.read(testRent.getId());
            Thread.sleep(500);
            retryCount++;
        }

        assertNotNull(retrievedRent, "Rent should be saved in MongoDB.");
        assertEquals(testRent.getRenter().getPersonalID(), retrievedRent.getRenter().getPersonalID());
        assertEquals(testRent.getVolume().getVolumeId(), retrievedRent.getVolume().getVolumeId());
    }
}
