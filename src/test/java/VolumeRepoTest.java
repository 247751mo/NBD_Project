//import jakarta.persistence.EntityManager;
//import jakarta.persistence.EntityManagerFactory;
//import jakarta.persistence.Persistence;
//import model.*;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Test;
//import repositories.VolumeRepo;
//
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class VolumeRepoTest {
//
//    private static VolumeRepo volumeRepo;
//
//    @BeforeAll
//    public static void setUp() {
//        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
//        EntityManager entityManager = entityManagerFactory.createEntityManager();
//        volumeRepo = new VolumeRepo(entityManager);
//    }
//    @Test
//    void testAddVolume() {
//        Book book = new Book("Henryk Sienkiewicz", "Potop", "Historyczne");
//        Monthly monthly = new Monthly("Top Gear", "Motoryzacyjne", "Immediate Media Company");
//
//        volumeRepo.create(book);
//        volumeRepo.create(monthly);
//
//        Book foundVolume1 = (Book) volumeRepo.read(book.getVolumeId());
//        Monthly foundVolume2 = (Monthly) volumeRepo.read(monthly.getVolumeId());
//
//        assertEquals(book, foundVolume1);
//        assertEquals(monthly, foundVolume2);
//    }
//    @Test
//    void testDeleteVolume() {
//        Book book = new Book("Adam Mickiewicz", "Pan Tadeusz", "Epika");
//        volumeRepo.create(book);
//
//        Book foundBook1 = (Book) volumeRepo.read(book.getVolumeId());
//        assertNotNull(foundBook1);
//
//        volumeRepo.delete(book);
//
//        Book foundBook2 = (Book) volumeRepo.read(book.getVolumeId());
//        assertNull(foundBook2);
//    }
//    @Test
//    void testUpdateVolume() {
//        Weekly weekly = new Weekly("Fakt", "Wiadomosci", "J. Billig");
//        volumeRepo.create(weekly);
//
//        Weekly foundWeekly1 = (Weekly) volumeRepo.read(weekly.getVolumeId());
//        assertEquals("Fakt", foundWeekly1.getTitle());
//        assertEquals("Wiadomosci", foundWeekly1.getGenre());
//
//        weekly.setTitle("Tygodnik Sportowy");
//        weekly.setGenre("Sportowe");
//
//        volumeRepo.update(weekly);
//
//        Weekly foundWeekly2 = (Weekly) volumeRepo.read(weekly.getVolumeId());
//        assertEquals("Tygodnik Sportowy", foundWeekly2.getTitle());
//        assertEquals("Sportowe", foundWeekly2.getGenre());
//    }
//    @Test
//    void testGetAllVolumes() {
//        List<Volume> volumes = volumeRepo.readAll();
//        int initialSize = volumes.size();
//
//        Book book1 = new Book("Boleslaw Prus", "Lalka", "Powiesc");
//        Book book2 = new Book("Sofokles", "Krol Edyp", "Tragedia");
//
//        volumeRepo.create(book1);
//        volumeRepo.create(book2);
//
//        volumes = volumeRepo.readAll();
//        int finalSize = volumes.size();
//
//        assertEquals(initialSize + 2, finalSize);
//    }
//
//
//}