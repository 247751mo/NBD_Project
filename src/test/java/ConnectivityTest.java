//import jakarta.persistence.EntityManager;
//import jakarta.persistence.EntityManagerFactory;
//import jakarta.persistence.Persistence;
//import model.Renter;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Test;
//
//
//public class ConnectivityTest {
//    private static EntityManagerFactory emf;
//    private static EntityManager em;
//    @BeforeAll
//    static void beforeAll()
//    {
//        emf = Persistence.createEntityManagerFactory("POSTGRES_RENT_PU");
//        em = emf.createEntityManager();
//    }
//    @Test
//    void testConnectivity(){
//        Renter renter = em.find(Renter.class, 1L);
//        assert(renter == null);
//    }
//
//}
