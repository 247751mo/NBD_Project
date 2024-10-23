package repositories;

import jakarta.persistence.EntityManager;
import model.Volume;
import java.util.List;
import java.util.UUID;

public class VolumeRepo implements Repo<Volume> {

    private final EntityManager em;

    public VolumeRepo(EntityManager entityManager) {
        this.em = entityManager;
    }

    @Override
    public Volume get(UUID id) {
        return em.find(Volume.class, id);
    }

    @Override
    public Volume add(Volume volume) {
        try {
            em.getTransaction().begin();
            em.persist(volume);
            em.getTransaction().commit();
            return volume;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Failed to add volume: " + volume.getVolumeId(), e);
        }
    }

    @Override
    public void delete(Volume volume) {
        try {
            em.getTransaction().begin();
            Volume managedVehicle = em.contains(volume) ? volume : em.merge(volume);
            em.remove(managedVehicle);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Failed to remove volume: " + volume.getVolumeId(), e);
        }
    }
    @Override
    public List<Volume> getAll() {
        return em.createQuery("SELECT v FROM Volume v", Volume.class).getResultList();
    }
    @Override
    public void update(Volume volume) {
        try {
            em.getTransaction().begin();
            em.merge(volume);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Failed to update volume: " + volume.getVolumeId(), e);
        }
    }
}