package dao;

import com.datastax.oss.driver.api.core.CqlSession;
import mapper.RentMapperBuilder;
import mapper.RenterMapperBuilder;
import mapper.VolumeMapperBuilder;
import model.Book;
import model.Rent;
import model.Renter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import repositories.CassandraRepository;
import repositories.RentRepository;
import repositories.RenterRepo;
import repositories.VolumeRepo;

public class RentDaoTest {
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

        Rent rent = new Rent(1, "1.01.2025", book, renter);
        rentDao.create(rent);
    }

    @Test
    void testgetbyif() {

        Rent rent = rentDao.findById(1);
        System.out.println(rent);
    }

    @Test
    void testDelete() {
        Book book1 = new Book(7, "book", "W pustyni i w puszczy", "Powiesc", "Henryk Sienkiewicz", false);
        Renter renter1 = new Renter(7, "Jan", "Kowalski");
        volumeDao.create(book1);
        renterDao.create(renter1);

        Rent rent1 = new Rent(2, "1 stycznia", book1, renter1);
        rentDao.create(rent1);
        Rent rent = rentDao.findById(2);
        System.out.println(rent);
        rentDao.remove(rent1);
        Rent rentt = rentDao.findById(2);
        Assertions.assertNull(rentt);
    }
}
