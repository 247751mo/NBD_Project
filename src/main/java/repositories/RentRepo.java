package repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.OptimisticLockException;
import model.Rent;
import model.Renter;
import model.Volume;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class RentRepo implements Repo<Rent> {

    private final EntityManager em;

    public RentRepo(EntityManager entityManager) {
        this.em = entityManager;
    }

    // Get Rent by UUID
    @Override
    public Rent get(UUID id) {
        return em.find(Rent.class, id);
    }

    // Get all Rents
    @Override
    public List<Rent> getAll() {
        return em.createQuery("SELECT r FROM Rent r", Rent.class).getResultList();
    }

    // Add a new Rent
    @Override
    public Rent add(Rent rent) {
        try {
            em.getTransaction().begin();
            em.persist(rent);
            em.getTransaction().commit();
            return rent;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Failed to add rent: " + rent.getId(), e);
        }
    }

    // Delete Rent
    @Override
    public void delete(Rent rent) {
        try {
            em.getTransaction().begin();
            Rent managedRent = em.contains(rent) ? rent : em.merge(rent);
            em.remove(managedRent);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Failed to delete rent: " + rent.getId(), e);
        }
    }

    // Update Rent
    @Override
    public void update(Rent rent) {
        try {
            em.getTransaction().begin();
            em.merge(rent);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Failed to update rent: " + rent.getId(), e);
        }
    }

    // Rent/Book a volume for a renter
    // Rent/Book a volume for a renter
    public void bookVolume(Renter renter, Volume volume, LocalDateTime rentStart) {
        try {
            em.getTransaction().begin();
            Volume managedVolume = em.find(Volume.class, volume.getVolumeId(), LockModeType.OPTIMISTIC_FORCE_INCREMENT);
            Renter managedRenter = em.find(Renter.class, renter.getId(), LockModeType.OPTIMISTIC_FORCE_INCREMENT);

            // Check if the volume is already rented or archived
            if (managedVolume.checkIfRented()) {
                throw new RuntimeException("The volume is already rented: " + volume.getTitle());
            }
            if (managedVolume.checkIfArchived()) {
                throw new RuntimeException("The volume is archived and cannot be rented: " + volume.getTitle());
            }

            // Create new rent
            Rent rent = new Rent(managedRenter, managedVolume, rentStart);
            em.persist(rent);

            // Increase renter's rent count
            em.merge(managedRenter);

            // Mark the volume as rented
            managedVolume.setRentedStatus(true);
            em.merge(managedVolume);

            em.getTransaction().commit();
        } catch (OptimisticLockException ole) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Optimistic lock exception: The volume or renter was modified concurrently: " + volume.getVolumeId(), ole);
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("The volume could not be booked: " + volume.getVolumeId(), e);
        }
    }


    // End a rent and return the volume
    public void returnVolume(Rent rent, LocalDateTime endTime) {
        try {
            em.getTransaction().begin();

            // End the rent
            rent.endRent(endTime);  // Set the end time of the rent
            em.merge(rent);

            // Retrieve and update the volume
            Volume volume = rent.getVolume();
            volume.setRentedStatus(false); // Mark volume as no longer rented
            em.merge(volume);

            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("The volume could not be returned: " + rent.getId(), e);
        }
    }
}
