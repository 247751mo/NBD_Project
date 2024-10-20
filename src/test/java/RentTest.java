import exceptions.ParameterException;
import managers.RentManager;
import managers.RenterManager;
import managers.VolumeManager;
import model.NoCard;
import model.Rent;
import model.Renter;
import model.Volume;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class RentTest {

    private Renter renter;
    private Volume volume;

    @BeforeEach
    void setUp() {
        // Setup test data
        renter = new Renter("John", "Doe", "123",new NoCard()); // assuming a constructor
        volume = new Volume("kkksiazka", "fantasy"); // assuming a constructor with volume size
    }

    @Test
    void testCreateRentWithValidParameters() {
        UUID rentId = UUID.randomUUID();
        LocalDateTime beginTime = LocalDateTime.now();

        Rent rent = new Rent(rentId, renter, volume, beginTime);

        assertEquals(rentId, rent.getId());
        assertEquals(renter, rent.getRenter());
        assertEquals(volume, rent.getVolume());
        assertEquals(beginTime, rent.getBeginTime());
        assertNull(rent.getEndTime());
    }

    @Test
    void testCreateRentWithNullRenter() {
        UUID rentId = UUID.randomUUID();
        LocalDateTime beginTime = LocalDateTime.now();

        Exception exception = assertThrows(ParameterException.class, () -> {
            new Rent(rentId, null, volume, beginTime);
        });

        assertEquals("Cannot create rent without renter!", exception.getMessage());
    }

    @Test
    void testCreateRentWithNullVolume() {
        UUID rentId = UUID.randomUUID();
        LocalDateTime beginTime = LocalDateTime.now();

        Exception exception = assertThrows(ParameterException.class, () -> {
            new Rent(rentId, renter, null, beginTime);
        });

        assertEquals("Cannot create rent without volume!", exception.getMessage());
    }

    @Test
    void testEndRentWithValidEndTime() {
        UUID rentId = UUID.randomUUID();
        LocalDateTime beginTime = LocalDateTime.now();
        LocalDateTime endTime = beginTime.plusHours(1);

        Rent rent = new Rent(rentId, renter, volume, beginTime);
        rent.endRent(endTime);

        assertEquals(endTime, rent.getEndTime());
    }


    @Test
    void testGetInfo() {
        UUID rentId = UUID.randomUUID();
        LocalDateTime beginTime = LocalDateTime.now();
        Rent rent = new Rent(rentId, renter, volume, beginTime);

        String expectedInfo = String.format("(Rent) rent id: %s %s %s begin time: %s, end time: null",
                rentId, renter.getInfo(), volume.volumeInfo(), beginTime);

        assertEquals(expectedInfo, rent.getInfo());
    }
}