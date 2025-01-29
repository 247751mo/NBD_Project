import model.Book;
import model.Monthly;
import model.Volume;
import org.bson.Document;
import org.junit.jupiter.api.*;
import repositories.VolumeRepo;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class VolumeRepoTest {

    private static VolumeRepo volumeRepo;


    @BeforeEach
    public void setUp() {
        volumeRepo = new VolumeRepo();
        volumeRepo.initDbConnection();
    }

    @AfterEach
    public void tearDown() {
        volumeRepo.getDatabase().getCollection("volumes", Volume.class).deleteMany(new Document());
        volumeRepo.close();
    }


    @Test
    void testAddVolume() {
        Book book1 = new Book(1, "Solaris", "Scifi", "Stanislaw Lem");
        volumeRepo.create(book1);
        Book foundVolume = (Book) volumeRepo.read(1);
        assertEquals(book1.getTitle(), foundVolume.getTitle());
        assertEquals(book1.getGenre(), foundVolume.getGenre());
        assertEquals(book1.getVolumeId(), foundVolume.getVolumeId());
        assertEquals(((Book) book1).getAuthor(), ((Book) foundVolume).getAuthor());

        assertEquals(book1, foundVolume);
    }


}