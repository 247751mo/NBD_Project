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

public class RenterRepoTest {

    private static final RedisRenterRepo REDIS_RENTER_REPO = new RedisRenterRepo();

    @BeforeEach
    public void setUp() {
        REDIS_RENTER_REPO.clearCache();
    }

    @AfterAll
    public static void tearDown() {
        REDIS_RENTER_REPO.clearCache();
        REDIS_RENTER_REPO.close();
    }

    @Test
    public void read_RenterInDB_RenterCachedAndReturned() {
        Renter renter = new Renter("12345678901", "John", "Doe");
        REDIS_RENTER_REPO.create(renter);

        Renter foundRenter = REDIS_RENTER_REPO.read("12345678901");
        assertEquals(renter, foundRenter);
    }

    @Test
    public void readAll_TwoRentersInMongo_TwoRentersCachedAndReturned() {
        // Dodanie dwóch renterów do MongoDB
        Renter renter1 = new Renter("12345678902", "Alice", "Smith");
        Renter renter2 = new Renter("12345678903", "Bob", "Johnson");
        REDIS_RENTER_REPO.create(renter1);
        REDIS_RENTER_REPO.create(renter2);

        List<Renter> addedRenters = List.of(renter1, renter2);
        List<Renter> foundRenters = REDIS_RENTER_REPO.readAll();
        assertEquals(2, foundRenters.size());
        Assertions.assertEquals(addedRenters.getFirst(), foundRenters.getFirst());
        Assertions.assertEquals(addedRenters.getLast(), foundRenters.getLast());

        assertEquals(renter1, REDIS_RENTER_REPO.read("12345678902"));
        assertEquals(renter2, REDIS_RENTER_REPO.read("12345678903"));
    }

    @Test
    public void add_ValidRenter_RenterAddedToMongoAndRedis() {
        Renter renter = new Renter("12345678904", "Eve", "Adams");
        REDIS_RENTER_REPO.create(renter);

        Renter redisRenter = REDIS_RENTER_REPO.read("12345678904");
        assertEquals(renter, redisRenter);
    }

    @Test
    public void update_UpdateRenter_RenterUpdatedInMongoAndRedis() {
        Renter renter = new Renter("12345678905", "Charlie", "Brown");
        REDIS_RENTER_REPO.create(renter);

        renter.setFirstName("UpdatedCharlie");
        REDIS_RENTER_REPO.update(renter);

        Renter updatedRedisRenter = REDIS_RENTER_REPO.read("12345678905");
        assertEquals("UpdatedCharlie", updatedRedisRenter.getFirstName());
    }

    @Test
    public void delete_RenterInMongoAndRedis_RenterDeleted() {
        Renter renter = new Renter("12345678906", "David", "Wilson");
        REDIS_RENTER_REPO.create(renter);

        assertNotNull(REDIS_RENTER_REPO.read("12345678906"));

        REDIS_RENTER_REPO.delete(renter);

        assertNull(REDIS_RENTER_REPO.read("12345678906"));
    }
}
