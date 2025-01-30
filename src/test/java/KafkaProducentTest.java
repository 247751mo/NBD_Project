import model.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import static model.KafkaProducent.sendRentAsync;
import static org.junit.jupiter.api.Assertions.*;

import repositories.*;

import java.time.LocalDateTime;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.bson.Document;
import org.junit.jupiter.api.*;

import java.util.concurrent.ExecutionException;


class KafkaProducentTest {

    private static RentRepo rentRepo;
    private static VolumeRepo volumeRepo;
    private static RenterRepo renterRepo;

    private Volume testVolume;
    private Renter testRenter;
    private Rent testRent;

    @BeforeEach
    void setup() throws ExecutionException, InterruptedException {
        // Initialize repositories and producer
        KafkaProducent producent = new KafkaProducent();

        rentRepo = new RentRepo();
        rentRepo.initDbConnection();

        volumeRepo = new VolumeRepo();
        volumeRepo.initDbConnection();

        renterRepo = new RenterRepo();
        renterRepo.initDbConnection();


    }

    @AfterAll
    static void tearDown() {
        // Clean up any inserted data and close repositories
        rentRepo.getDatabase().getCollection("rents", Rent.class).deleteMany(new Document());
        rentRepo.getDatabase().getCollection("archived", Rent.class).deleteMany(new Document());
        rentRepo.close();

        renterRepo.getDatabase().getCollection("renters", Renter.class).deleteMany(new Document());
        renterRepo.close();

        volumeRepo.getDatabase().getCollection("volumes", Volume.class).deleteMany(new Document());
        volumeRepo.close();
    }

    @Test
    @DisplayName("Test adding a new Rent and sending it via Kafka")
    void testAddRentNoMongo() throws InterruptedException, JsonProcessingException {
        testRenter = new Renter("124", "Doe", "Jane");
        renterRepo.create(testRenter);

        testVolume = new Book(1, "vol1", "First Book", "Fantasy");
        volumeRepo.create(testVolume);

        testRent = new Rent(testRenter, testVolume, LocalDateTime.now());

        sendRentAsync(testRent);

        assertNotNull(testRent, "Rent object should not be null");
        System.out.println("Rent was sent to Kafka with ID: " + testRent.getId());
    }

}