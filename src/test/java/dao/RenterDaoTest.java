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
    void test() {
        Renter renter = new Renter(1, "Jan", "Kowalski");
        renterDao.create(renter);
    }

    @Test
    void getRenterById() {
        Renter retrievedRenter = renterDao.findById(1);
        Assertions.assertNotNull(retrievedRenter, "Renter should not be null");
        Assertions.assertEquals("Jan", retrievedRenter.getFirstName());
        Assertions.assertEquals("Kowalski", retrievedRenter.getLastName());
    }
}
