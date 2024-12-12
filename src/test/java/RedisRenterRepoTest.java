import static org.junit.jupiter.api.Assertions.*;

import model.Renter;
import org.bson.Document;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repositories.MongoRenterRepo;
import repositories.RedisRenterRepo;
import repositories.RenterRepo;

import java.util.List;

public class RedisRenterRepoTest {

    private static final MongoRenterRepo MONGO_RENTER_REPO = new MongoRenterRepo();
    private static final RedisRenterRepo REDIS_RENTER_REPO = new RedisRenterRepo();
    private static final RenterRepo RENTER_REPOSITORY = new RenterRepo(REDIS_RENTER_REPO, MONGO_RENTER_REPO);

    @BeforeEach
    public void setUp() {
        // Oczyszczanie kolekcji MongoDB i Redis przed każdym testem
        MONGO_RENTER_REPO.getDatabase().getCollection("renters", Renter.class).deleteMany(new Document());
        REDIS_RENTER_REPO.clearCache();
    }

    @AfterAll
    public static void tearDown() {
        // Oczyszczanie kolekcji MongoDB i Redis po wszystkich testach
        MONGO_RENTER_REPO.getDatabase().getCollection("renters", Renter.class).deleteMany(new Document());
        REDIS_RENTER_REPO.clearCache();
        REDIS_RENTER_REPO.close();
    }

    @Test
    public void findById_RenterInDB_RenterCachedAndReturned() {
        // Dodanie Rentera do MongoDB
        Renter renter = new Renter("12345678901", "John", "Doe");
        RENTER_REPOSITORY.create(renter);

        // Weryfikacja, że Renter jest zwracany z Mongo
        Renter foundRenter = RENTER_REPOSITORY.read("12345678901");
        assertEquals(renter, foundRenter);

        // Weryfikacja, że Renter został zapisany w Redis
        Renter cachedRenter = REDIS_RENTER_REPO.read("12345678901");
        assertEquals(renter, cachedRenter);
    }

    @Test
    public void findAll_TwoRentersInMongo_TwoRentersCachedAndReturned() {
        // Dodanie dwóch renterów do MongoDB
        Renter renter1 = new Renter("12345678902", "Alice", "Smith");
        Renter renter2 = new Renter("12345678903", "Bob", "Johnson");
        RENTER_REPOSITORY.create(renter1);
        RENTER_REPOSITORY.create(renter2);

        List<Renter> addedRenters = List.of(renter1, renter2);
        List<Renter> foundRenters = RENTER_REPOSITORY.readAll();
        assertEquals(2, foundRenters.size());
        Assertions.assertEquals(addedRenters.getFirst(), foundRenters.getFirst());
        Assertions.assertEquals(addedRenters.getLast(), foundRenters.getLast());


        // Weryfikacja, że Renters zostały zapisane w Redis
        assertEquals(renter1, REDIS_RENTER_REPO.read("12345678902"));
        assertEquals(renter2, REDIS_RENTER_REPO.read("12345678903"));
    }

    @Test
    public void add_ValidRenter_RenterAddedToMongoAndRedis() {
        // Dodanie Rentera do repozytorium
        Renter renter = new Renter("12345678904", "Eve", "Adams");
        RENTER_REPOSITORY.create(renter);

        // Weryfikacja dodania Rentera do MongoDB
        Renter mongoRenter = MONGO_RENTER_REPO.read("12345678904");
        assertEquals(renter, mongoRenter);

        // Weryfikacja dodania Rentera do Redis
        Renter redisRenter = REDIS_RENTER_REPO.read("12345678904");
        assertEquals(renter, redisRenter);
    }

    @Test
    public void update_UpdateRenter_RenterUpdatedInMongoAndRedis() {
        // Dodanie Rentera do repozytorium
        Renter renter = new Renter("12345678905", "Charlie", "Brown");
        RENTER_REPOSITORY.create(renter);

        // Aktualizacja Rentera
        renter.setFirstName("UpdatedCharlie");
        RENTER_REPOSITORY.update(renter);

        // Weryfikacja aktualizacji w MongoDB
        Renter updatedMongoRenter = MONGO_RENTER_REPO.read("12345678905");
        assertEquals("UpdatedCharlie", updatedMongoRenter.getFirstName());

        // Weryfikacja aktualizacji w Redis
        Renter updatedRedisRenter = REDIS_RENTER_REPO.read("12345678905");
        assertEquals("UpdatedCharlie", updatedRedisRenter.getFirstName());
    }

    @Test
    public void delete_RenterInMongoAndRedis_RenterDeleted() {
        // Dodanie Rentera do repozytorium
        Renter renter = new Renter("12345678906", "David", "Wilson");
        RENTER_REPOSITORY.create(renter);

        // Weryfikacja, że Renter istnieje w MongoDB i Redis przed usunięciem
        assertNotNull(MONGO_RENTER_REPO.read("12345678906"));
        assertNotNull(REDIS_RENTER_REPO.read("12345678906"));
        assertNotNull(RENTER_REPOSITORY.read("12345678906"));

        // Usunięcie Rentera
        RENTER_REPOSITORY.delete(renter);

        // Weryfikacja usunięcia Rentera z MongoDB
        assertNull(MONGO_RENTER_REPO.read("12345678906"));

        // Weryfikacja usunięcia Rentera z Redis
        assertNull(REDIS_RENTER_REPO.read("12345678906"));
    }
}
