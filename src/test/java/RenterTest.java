import model.NoCard;
import model.Renter;
import model.RenterType;
import exceptions.ParameterException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RenterTest {

    private Renter renter;
    private RenterType renterType;

    @BeforeEach
    void setUp() {
        renterType = new NoCard(); // Assuming NoCard implements RenterType
        renter = new Renter("John", "Doe", "12345", renterType);
    }

    @Test
    void testValidRenterCreation() {
        assertNotNull(renter);
        assertEquals("John", renter.getFirstName());
        assertEquals("Doe", renter.getLastName());
        assertEquals("12345", renter.getPersonalID());
        assertEquals(renterType, renter.getType());
        assertFalse(renter.checkIfArchived());
    }

    @Test
    void testInvalidFirstName() {
        Exception exception = assertThrows(ParameterException.class, () -> {
            new Renter("", "Doe", "12345", renterType);
        });
        assertEquals("Invalid firstName (can't be empty)!", exception.getMessage());
    }

    @Test
    void testInvalidLastName() {
        Exception exception = assertThrows(ParameterException.class, () -> {
            new Renter("John", "", "12345", renterType);
        });
        assertEquals("Invalid lastName (can't be empty)!", exception.getMessage());
    }

    @Test
    void testInvalidPersonalID() {
        Exception exception = assertThrows(ParameterException.class, () -> {
            new Renter("John", "Doe", "", renterType);
        });
        assertEquals("Invalid personalID (can't be empty)!", exception.getMessage());
    }

    @Test
    void testSetFirstName() {
        renter.setFirstName("Jane");
        assertEquals("Jane", renter.getFirstName());
    }

    @Test
    void testSetFirstNameEmpty() {
        renter.setFirstName(""); // Attempt to set to an invalid value
        assertEquals("John", renter.getFirstName()); // Should remain unchanged
    }

    @Test
    void testSetLastName() {
        renter.setLastName("Smith");
        assertEquals("Smith", renter.getLastName());
    }

    @Test
    void testSetLastNameEmpty() {
        renter.setLastName(""); // Attempt to set to an invalid value
        assertEquals("Doe", renter.getLastName()); // Should remain unchanged
    }

    @Test
    void testSetRenterType() {
        RenterType newType = new NoCard(); // Assuming NoCard implements RenterType
        renter.setType(newType);
        assertEquals(newType, renter.getType());
    }

    @Test
    void testSetArchiveStatus() {
        renter.setArchiveStatus(true);
        assertTrue(renter.checkIfArchived());
    }

    @Test
    void testGetInfo() {
        String expectedInfo = "(Renter) first name: John, last name: Doe, personal id: 12345, renter type: " + renterType.getRenterTypeInfo() + ", is archived: false";
        assertEquals(expectedInfo, renter.getInfo());
    }

    @Test
    void testMaxVolumes() {
        int volumes = 5;
        int expectedMaxVolumes = renterType.maxVolumes(volumes); // Assuming maxVolumes is defined in RenterType
        assertEquals(expectedMaxVolumes, renter.maxVolumes(volumes));
    }
}
