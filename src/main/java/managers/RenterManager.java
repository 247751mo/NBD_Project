package managers;

import exceptions.DuplicateException;
import model.Renter;
import repositories.RenterRepo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.Optional;

public class RenterManager {
    private final EntityManagerFactory entityManagerFactory;
    private final RenterRepo renterRepo;

    // Constructor
    public RenterManager(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
        this.renterRepo = new RenterRepo();
    }

    // Check if a renter with the same personal ID exists and if not, add to the repo
    public boolean checkRenter(Renter renter) throws DuplicateException {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            TypedQuery<Renter> query = entityManager.createQuery(
                    "SELECT r FROM Renter r WHERE r.personalID = :personalID", Renter.class);
            query.setParameter("personalID", renter.getPersonalID());
            List<Renter> results = query.getResultList();

            if (!results.isEmpty()) {
                throw new DuplicateException("Renter with this personal ID already exists.");
            }

            // Adding the renter to the repository (the DB in this case)
            renterRepo.addRenter(renter);
            return true;
        } finally {
            entityManager.close();
        }
    }

    // Count the number of renters in the database
    public int countRenters() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            TypedQuery<Long> query = entityManager.createQuery("SELECT COUNT(r) FROM Renter r", Long.class);
            return query.getSingleResult().intValue();
        } finally {
            entityManager.close();
        }
    }

    // Get all renters' information
    public String getAllRentersInfo() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        StringBuilder result = new StringBuilder();
        try {
            TypedQuery<Renter> query = entityManager.createQuery("SELECT r FROM Renter r", Renter.class);
            List<Renter> renters = query.getResultList();

            for (Renter renter : renters) {
                result.append("First Name: ").append(renter.getFirstName())
                        .append(", Last Name: ").append(renter.getLastName())
                        .append(", ID: ").append(renter.getPersonalID())
                        .append(", Type: ").append(renter.getType().getRenterTypeInfo())
                        .append(", Is Archived: ").append(renter.checkIfArchived())
                        .append("\n");
            }
        } finally {
            entityManager.close();
        }
        return result.toString();
    }
}
