package model;


import org.bson.Document;
import repositories.MongoRenterRepo;
import repositories.RedisRenterRepo;
import repositories.RenterRepo;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
@Fork(2)
@Warmup(iterations = 5, time = 5, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 10, timeUnit = TimeUnit.SECONDS)
public class BenchmarkTest {

    private final MongoRenterRepo MONGO_RENTER_REPO = new MongoRenterRepo();
    private final RedisRenterRepo REDIS_RENTER_REPO = new RedisRenterRepo();
    private final RenterRepo RENTER_REPOSITORY = new RenterRepo(REDIS_RENTER_REPO, MONGO_RENTER_REPO);

    private final Renter renter = new Renter("3333333333", "FirstName", "LastName");
    private final Renter setupRenter = new Renter("222222222", "FirstName", "LastName");
    private final String renterPersonalID = setupRenter.getPersonalID();

    @Setup(Level.Invocation)
    public void setUp() {
        MONGO_RENTER_REPO.getDatabase().getCollection("renters", Renter.class).deleteMany(new Document());
        REDIS_RENTER_REPO.clearCache();
        RENTER_REPOSITORY.create(setupRenter);
    }

    @TearDown
    public void tearDown() {
        // Czyszczenie bazy danych po każdej próbie
        MONGO_RENTER_REPO.getDatabase().getCollection("renters", Renter.class).deleteMany(new Document());
        REDIS_RENTER_REPO.clearCache();
        REDIS_RENTER_REPO.close();
    }

    // Benchmark dla operacji zapisu do MongoDB
    @Benchmark
    public void MongoSet() {
        MONGO_RENTER_REPO.create(renter);
    }

    // Benchmark dla operacji odczytu z MongoDB
    @Benchmark
    public Renter MongoGet() {
        return MONGO_RENTER_REPO.read(renterPersonalID);
    }

    // Benchmark dla operacji zapisu do Redisa
    @Benchmark
    public void RedisSet() {
        REDIS_RENTER_REPO.create(renter);
    }

    // Benchmark dla operacji odczytu z Redisa
    @Benchmark
    public Renter RedisGet() {
        return REDIS_RENTER_REPO.read(renterPersonalID);
    }

    // Benchmark dla operacji zapisu do repozytorium z dekoratorem
    @Benchmark
    public void DecoratorSet() {
        RENTER_REPOSITORY.create(renter);
    }

    // Benchmark dla operacji odczytu z repozytorium z dekoratorem
    @Benchmark
    public Renter DecoratorGet() {
        return RENTER_REPOSITORY.read(renterPersonalID);
    }
}
