package managers;

import model.Rent;
import model.Volume;
import repositories.RentRepo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import exceptions.ParameterException;

import java.util.List;

public class RentManager {
    private final RentRepo rentRepo;
    private final EntityManagerFactory entityManagerFactory;
    private final EntityManager entityManager;

    // Konstruktor, inicjuje repozytorium i EntityManager
    public RentManager() {
        this.rentRepo = new RentRepo();
        this.entityManagerFactory = Persistence.createEntityManagerFactory("my-persistence-unit");
        this.entityManager = entityManagerFactory.createEntityManager();
    }

    // Dodanie wypożyczenia, jeśli dostępny jest odpowiedni wolumen
    public boolean createRent(List<Volume> volumes, Rent rent) {
        entityManager.getTransaction().begin();

        try {
            // Sprawdzenie dostępności wolumenów
            for (Volume volume : volumes) {
                List<Rent> existingRents = rentRepo.getAllRents();
                boolean isAvailable = existingRents.stream()
                        .noneMatch(existingRent -> existingRent.getVolume().equals(volume));

                if (isAvailable) {
                    rent.setVolume(volume);
                    rentRepo.addRent(rent, existingRents);
                    entityManager.persist(rent);
                    entityManager.getTransaction().commit();
                    return true;
                }
            }

            // Jeśli żaden wolumen nie jest dostępny, wycofaj transakcję
            entityManager.getTransaction().rollback();
            return false;
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw new ParameterException("Failed to create rent: " + e.getMessage());
        }
    }

    // Zwraca liczbę wszystkich wypożyczeń
    public int countRents() {
        return rentRepo.getAllRents().size();
    }

    // Metoda zamykająca EntityManager i EntityManagerFactory
    public void close() {
        if (entityManager.isOpen()) {
            entityManager.close();
        }
        if (entityManagerFactory.isOpen()) {
            entityManagerFactory.close();
        }
    }
}
