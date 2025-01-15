package dao;

import com.datastax.oss.driver.api.core.CqlSession;
import mapper.RenterMapperBuilder;
import model.Renter;
import repositories.CassandraRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import repositories.RenterRepo;

class RenterDaoTest {
    private static RenterDao renterDao;
    private static CqlSession session;
    private static RenterRepo renterRepo;

    @BeforeAll
    static void setUp() {
        CassandraRepository cassandraRepository = new CassandraRepository();
        cassandraRepository.initSession();
        session = cassandraRepository.getSession();
        renterRepo = new RenterRepo(session);
        renterDao = new RenterMapperBuilder(session).build().renterDao();
    }

    @Test
    void create() {
        Renter renter = new Renter(1, "Jan", "Kowalski");
        renterDao.create(renter);
    }

    @Test
    void getRenterById() {
        Renter renter = new Renter(1, "Jan", "Kowalski");
        renterDao.create(renter);
        Renter retrievedRenter = renterDao.findById(1);
        Assertions.assertEquals("Jan", retrievedRenter.getFirstName());
        Assertions.assertEquals("Kowalski", retrievedRenter.getLastName());
    }
    @Test
    void updateRenter() {
        Renter renter = new Renter(2, "Janusz", "Nowak");
        String newName = "Jan";
        renter.setFirstName(newName);
        renterDao.update(renter);
        Renter retrievedRenter = renterDao.findById(2);
        Assertions.assertEquals("Jan", retrievedRenter.getFirstName());
        Assertions.assertEquals("Nowak", retrievedRenter.getLastName());
    }

    @Test
    void deleteRenter() {
        Renter renter = new Renter(3, "Pawel", "Marczak");
        renterDao.create(renter);
        Assertions.assertNotNull(renterDao.findById(3));
        renterDao.remove(3);
        Renter retrievedRenter = renterDao.findById(3);
        Assertions.assertNull(retrievedRenter);
    }
}
