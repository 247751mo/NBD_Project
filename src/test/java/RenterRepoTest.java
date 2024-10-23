//import model.NoCard;
//import model.Renter;
//import model.RenterType;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import repositories.RenterRepo;
//
//import java.util.List;
//import java.util.UUID;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class RenterRepoTest {
//
//    private RenterRepo renterRepo;
//    private Renter renter;
//
//    @BeforeEach
//    void setUp() {
//        renterRepo = new RenterRepo();
//        renter = new Renter("John", "Doe", "12345", new NoCard()); // Assuming NoCard implements RenterType
//    }
//
//    @Test
//    void testAddRenter() {
//        renterRepo.addRenter(renter);
//
//        List<Renter> renters = renterRepo.getAll();
//        assertEquals(1, renters.size());
//        assertEquals(renter, renters.get(0));
//    }
//
//    @Test
//    void testAddMultipleRenters() {
//        Renter anotherRenter = new Renter("Jane", "Smith", "67890", new NoCard());
//        renterRepo.addRenter(renter);
//        renterRepo.addRenter(anotherRenter);
//
//        List<Renter> renters = renterRepo.getAll();
//        assertEquals(2, renters.size());
//        assertTrue(renters.contains(renter));
//        assertTrue(renters.contains(anotherRenter));
//    }
//
//    @Test
//    void testRemoveRenter() {
//        renterRepo.addRenter(renter); // Add renter to be removed
//
//        renterRepo.removeRenter(renter); // Remove the added renter
//        List<Renter> renters = renterRepo.getAll();
//
//        assertEquals(0, renters.size()); // Ensure renter has been removed
//    }
//
//    @Test
//    void testRemoveNonExistentRenter() {
//        Renter nonExistentRenter = new Renter("Non", "Existent", "99999", new NoCard());
//
//        renterRepo.addRenter(renter); // Add one renter
//        renterRepo.removeRenter(nonExistentRenter); // Try to remove a renter that does not exist
//
//        List<Renter> renters = renterRepo.getAll();
//        assertEquals(1, renters.size()); // The original renter should still be present
//        assertEquals(renter, renters.get(0));
//    }
//
//    @Test
//    void testGetAllRenters() {
//        assertTrue(renterRepo.getAll().isEmpty()); // Initially, the list should be empty
//
//        renterRepo.addRenter(renter); // Add a renter
//        List<Renter> renters = renterRepo.getAll();
//
//        assertEquals(1, renters.size());
//        assertEquals(renter, renters.get(0)); // The renter added should be retrievable
//    }
//}
