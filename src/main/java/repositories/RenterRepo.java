package repositories;

import jakarta.persistence.EntityManager;
import model.Renter;
import java.util.List;
import java.util.UUID;

public class RenterRepo implements Repo<Renter> {

    private final EntityManager em;

    public RenterRepo(EntityManager entityManager) {
        this.em = entityManager;
    }

    @Override
    public Renter get(UUID id) {
        return em.find(Renter.class, id);
    }
    @Override
    public List<Renter> getAll() {
        return em.createQuery("SELECT r FROM Renter r", Renter.class).getResultList();
    }
    @Override
    public Renter add(Renter renter) {
        try {
            em.getTransaction().begin();
            em.persist(renter);
            em.getTransaction().commit();
            return renter;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Failed to add renter: " + renter.getId(), e);
        }
    }
    @Override
    public void delete(Renter renter) {
        try {
            em.getTransaction().begin();
            Renter managedRenter = em.contains(renter) ? renter : em.merge(renter);
            em.remove(managedRenter);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Failed to remove renter: " + renter.getId(), e);
        }
    }
    @Override
    public void update(Renter renter) {
        try {
            em.getTransaction().begin();
            em.merge(renter);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Failed to update renter: " + renter.getId(), e);
        }
    }
}