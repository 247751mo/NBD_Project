package managers;
import model.Renter;
import java.util.List;
import model.Renter;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class RenterManager {

    private EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("my-persistence-unit");

    public void saveRenter(Renter renter) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(renter);
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    public List<Renter> getAllRenters() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        List<Renter> renters = entityManager.createQuery("SELECT r FROM renters r", Renter.class).getResultList();
        entityManager.close();
        return renters;
    }
}
