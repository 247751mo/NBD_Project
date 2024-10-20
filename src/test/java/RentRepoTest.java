import model.NoCard;
import model.Rent;
import model.Renter;
import model.Volume;
import exceptions.ParameterException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repositories.RentRepo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class RentRepoTest {

    private RentRepo rentRepo;
    private Rent rent;
    private Renter renter;
    private Volume volume;

    @BeforeEach
    void setUp() {
        rentRepo = new RentRepo();
        UUID rentId = UUID.randomUUID();
        renter = new Renter("John", "Doe", "123",new NoCard()); // assuming a constructor
        volume = new Volume("kkksiazka", "fantasy"); // assuming a constructor with volume size
        rent = new Rent(rentId, renter, volume, LocalDateTime.now());
    }

    @Test
    void testAddRent() {
        rentRepo.addRent(rent, null); // Second parameter is not used in the method

        List<Rent> rents = rentRepo.getAllRents();
        assertEquals(1, rents.size());
        assertEquals(rent, rents.get(0));
    }

    @Test
    void testAddNullRent() {
        rentRepo.addRent(null, null); // Attempt to add null rent

        List<Rent> rents = rentRepo.getAllRents();
        assertEquals(0, rents.size()); // No rents should be added
    }

    @Test
    void testRemoveRent() {
        rentRepo.addRent(rent, null); // Add rent to be removed

        rentRepo.removeRent(rent); // Remove the added rent
        List<Rent> rents = rentRepo.getAllRents();

        assertEquals(0, rents.size()); // Ensure rent has been removed
    }

    @Test
    void testRemoveNonExistentRent() {
        Rent rentToRemove = new Rent(UUID.randomUUID(), new Renter("John", "Doe", "123",new NoCard()), new Volume("kkksiazka", "fantasy"), LocalDateTime.now());

        rentRepo.addRent(rent, null); // Add one rent
        rentRepo.removeRent(rentToRemove); // Try to remove a rent that does not exist

        List<Rent> rents = rentRepo.getAllRents();
        assertEquals(1, rents.size()); // The original rent should still be present
        assertEquals(rent, rents.get(0));
    }

    @Test
    void testGetAllRents() {
        assertTrue(rentRepo.getAllRents().isEmpty()); // Initially, the list should be empty

        rentRepo.addRent(rent, null); // Add a rent
        List<Rent> rents = rentRepo.getAllRents();

        assertEquals(1, rents.size());
        assertEquals(rent, rents.get(0)); // The rent added should be retrievable
    }
}
