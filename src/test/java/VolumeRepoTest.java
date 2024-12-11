import model.Book;
import model.Monthly;
import model.Volume;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repositories.MongoVolumeRepo;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class VolumeRepoTest {

    private static MongoVolumeRepo mongoVolumeRepo;

    @BeforeAll
    public static void setUp() {
        mongoVolumeRepo = new MongoVolumeRepo();
    }

    @BeforeEach
    public void cleanUp() {
        mongoVolumeRepo.getDatabase().getCollection("volumes", Volume.class).drop();
    }

    @AfterAll
    public static void tearDown() {
        mongoVolumeRepo.getDatabase().getCollection("volumes", Volume.class).drop();
        mongoVolumeRepo.close();
    }

    @Test
    void testAddVolume() {
        Book book1 = new Book(1,"Solaris", "Scifi", "Stanislaw Lem");
        mongoVolumeRepo.create(book1);
        Book foundVolume = (Book) mongoVolumeRepo.read(1);
        assertEquals(book1.getTitle(), foundVolume.getTitle());
        assertEquals(book1.getGenre(), foundVolume.getGenre());
        assertEquals(book1.getVolumeId(), foundVolume.getVolumeId());
        assertEquals(((Book) book1).getAuthor(), ((Book) foundVolume).getAuthor());

        assertEquals(book1, foundVolume);
    }



    @Test
    void testRemoveVolume() {
        Volume book1 = new Book(1,"Solaris","Scifi","Stanislaw Lem");
        mongoVolumeRepo.create(book1);

        Volume foundVolume = mongoVolumeRepo.read(book1.getVolumeId());
        assertNotNull(foundVolume);

        mongoVolumeRepo.delete(book1);

        Volume deletedVolume = mongoVolumeRepo.read(book1.getVolumeId());
        assertNull(deletedVolume);
    }

    @Test
    void testUpdateVolume() {
        Volume book1 = new Book(1,"Solaris","Scifi","Stanislaw Lem");
        mongoVolumeRepo.create(book1);

        Volume foundVolumeBeforeUpdate = mongoVolumeRepo.read(book1.getVolumeId());
        assertEquals("Solaris", foundVolumeBeforeUpdate.getTitle());
        assertEquals("Scifi", foundVolumeBeforeUpdate.getGenre());

        book1.setTitle("NowyTytul");
        book1.setGenre("ificS");
        book1.setIsRented(1);

        mongoVolumeRepo.update(book1);

        Volume foundVolumeAfterUpdate = mongoVolumeRepo.read(book1.getVolumeId());
        assertEquals("NowyTytul", foundVolumeAfterUpdate.getTitle());
        assertEquals("ificS", foundVolumeAfterUpdate.getGenre());

    }

    @Test
    void testGetAllVolumes() {
        List<Volume> volumes = mongoVolumeRepo.readAll();
        int initialSize = volumes.size();

        Volume book1 = new Book(1,"Solaris","Scifi","Stanislaw Lem");
        Volume monthly1 = new Monthly(2, "Miesiecznik","Gatunek","Wydawca");

        mongoVolumeRepo.create(book1);
        mongoVolumeRepo.create(monthly1);

        volumes = mongoVolumeRepo.readAll();
        int finalSize = volumes.size();

        assertEquals(initialSize + 2, finalSize);
    }
}
