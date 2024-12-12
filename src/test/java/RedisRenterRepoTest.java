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
        MONGO_RENTER_REPO.getDatabase().getCollection("renters", Renter.class).deleteMany(new Document());
        REDIS_RENTER_REPO.clearCache();
    }

    @AfterAll
    public static void tearDown() {
        MONGO_RENTER_REPO.getDatabase().getCollection("renters", Renter.class).deleteMany(new Document());
        REDIS_RENTER_REPO.clearCache();
        REDIS_RENTER_REPO.close();
    }

    @Test
    public void read_RenterInDB_RenterCachedAndReturned() {
        Renter renter = new Renter("12345678901", "John", "Doe");
        RENTER_REPOSITORY.create(renter);

        Renter foundRenter = RENTER_REPOSITORY.read("12345678901");
        assertEquals(renter, foundRenter);

        Renter cachedRenter = REDIS_RENTER_REPO.read("12345678901");
        assertEquals(renter, cachedRenter);
    }

    @Test
    public void readAll_TwoRentersInMongo_TwoRentersCachedAndReturned() {
        Renter renter1 = new Renter("12345678902", "Alice", "Smith");
        Renter renter2 = new Renter("12345678903", "Bob", "Johnson");
        RENTER_REPOSITORY.create(renter1);
        RENTER_REPOSITORY.create(renter2);

        List<Renter> addedRenters = List.of(renter1, renter2);
        List<Renter> foundRenters = RENTER_REPOSITORY.readAll();
        assertEquals(2, foundRenters.size());
        Assertions.assertEquals(addedRenters.getFirst(), foundRenters.getFirst());
        Assertions.assertEquals(addedRenters.getLast(), foundRenters.getLast());

        assertEquals(renter1, REDIS_RENTER_REPO.read("12345678902"));
        assertEquals(renter2, REDIS_RENTER_REPO.read("12345678903"));
    }

    @Test
    public void add_ValidRenter_RenterAddedToMongoAndRedis() {
        Renter renter = new Renter("12345678904", "Eve", "Adams");
        RENTER_REPOSITORY.create(renter);

        Renter mongoRenter = MONGO_RENTER_REPO.read("12345678904");
        assertEquals(renter, mongoRenter);

        Renter redisRenter = REDIS_RENTER_REPO.read("12345678904");
        assertEquals(renter, redisRenter);
    }

    @Test
    public void update_UpdateRenter_RenterUpdatedInMongoAndRedis() {
        Renter renter = new Renter("12345678905", "Charlie", "Brown");
        RENTER_REPOSITORY.create(renter);

        renter.setFirstName("UpdatedCharlie");
        RENTER_REPOSITORY.update(renter);

        Renter updatedMongoRenter = MONGO_RENTER_REPO.read("12345678905");
        assertEquals("UpdatedCharlie", updatedMongoRenter.getFirstName());
        Renter updatedRedisRenter = REDIS_RENTER_REPO.read("12345678905");
        assertEquals("UpdatedCharlie", updatedRedisRenter.getFirstName());
    }

    @Test
    public void delete_RenterInMongoAndRedis_RenterDeleted() {
        Renter renter = new Renter("12345678906", "David", "Wilson");
        RENTER_REPOSITORY.create(renter);

        assertNotNull(MONGO_RENTER_REPO.read("12345678906"));
        assertNotNull(REDIS_RENTER_REPO.read("12345678906"));
        assertNotNull(RENTER_REPOSITORY.read("12345678906"));

        RENTER_REPOSITORY.delete(renter);

        assertNull(MONGO_RENTER_REPO.read("12345678906"));
        assertNull(REDIS_RENTER_REPO.read("12345678906"));
    }
}
