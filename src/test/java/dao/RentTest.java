package dao;

import com.datastax.oss.driver.api.core.CqlSession;
import model.Book;
import model.Rent;
import model.Renter;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import repositories.CassandraRepository;
import repositories.RentRepository;
import repositories.RenterRepo;
import repositories.VolumeRepo;

public class RentTest {
    private static CqlSession session;
    private static RentRepository rentRepo;
    private static RentDao rentDao;
    private static VolumeDao volumeDao;
    private static VolumeRepo volumeRepo;
    private static RenterRepo renterRepo;
    private static RenterDao renterDao;

    @BeforeAll
    static void setUp() {
        CassandraRepository cassandraRepository = new CassandraRepository();
        cassandraRepository.initSession();
        session = cassandraRepository.getSession();
        volumeRepo = new VolumeRepo(session);
        volumeDao = new VolumeMapperBuilder(session).build().volumeDao();
        renterRepo = new RenterRepo(session);
        renterDao = new RenterMapperBuilder(session).build().renterDao();
        rentDao = new RentMapperBuilder(session).build().rentDao();
        rentRepo = new RentRepository(session);
    }

    @Test
    void testRent() {
        Book book = new Book(5, "book", "W pustyni i w puszczy", "Powiesc", "Henryk Sienkiewicz", false);
        Renter renter = new Renter(5, "Jan", "Kowalski");
        volumeDao.create(book);
        renterDao.create(renter);

        Rent rent = new Rent(1, "1 stycznia", book, renter);
        rentDao.create(rent);
    }

    @Test
    void testgetbyif() {

        Rent rent = rentDao.findById(1);
        System.out.println(rent);
    }
}
