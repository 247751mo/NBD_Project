import model.Book;
import model.Monthly;
import model.Volume;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repositories.VolumeRepo;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class VolumeRepoTest {

    private static VolumeRepo volumeRepo;

    @BeforeAll
    public static void setUp() {
        volumeRepo = new VolumeRepo();
    }

    @BeforeEach
    public void cleanUp() {
        volumeRepo.getDatabase().getCollection("volumes", Volume.class).drop();
    }

    @AfterAll
    public static void tearDown() {
        volumeRepo.getDatabase().getCollection("volumes", Volume.class).drop();
        volumeRepo.close();
    }

    @Test
    void testAddVolume() {
        Book book1 = new Book(1,"Solaris", "Scifi", "Stanislaw Lem");
        volumeRepo.create(book1);
        Book foundVolume = (Book) volumeRepo.read(1);
        assertEquals(book1.getTitle(), foundVolume.getTitle());
        assertEquals(book1.getGenre(), foundVolume.getGenre());
        assertEquals(book1.getVolumeId(), foundVolume.getVolumeId());
        assertEquals(((Book) book1).getAuthor(), ((Book) foundVolume).getAuthor());

        // Porównanie całego obiektu
        assertEquals(book1, foundVolume);
    }



    @Test
    void testRemoveVolume() {
        Volume book1 = new Book(1,"Solaris","Scifi","Stanislaw Lem");
        volumeRepo.create(book1);

        Volume foundVolume = volumeRepo.read(book1.getVolumeId());
        assertNotNull(foundVolume);

        volumeRepo.delete(book1);

        Volume deletedVolume = volumeRepo.read(book1.getVolumeId());
        assertNull(deletedVolume);
    }

    @Test
    void testUpdateVolume() {
        Volume book1 = new Book(1,"Solaris","Scifi","Stanislaw Lem");
        volumeRepo.create(book1);

        Volume foundVolumeBeforeUpdate = volumeRepo.read(book1.getVolumeId());
        assertEquals("Solaris", foundVolumeBeforeUpdate.getTitle());
        assertEquals("Scifi", foundVolumeBeforeUpdate.getGenre());

        book1.setTitle("NowyTytul");
        book1.setGenre("ificS");
        book1.setRented(true);

        volumeRepo.update(book1);

        Volume foundVolumeAfterUpdate = volumeRepo.read(book1.getVolumeId());
        assertEquals("NowyTytul", foundVolumeAfterUpdate.getTitle());
        assertEquals("ificS", foundVolumeAfterUpdate.getGenre());

    }

    @Test
    void testGetAllVolumes() {
        List<Volume> volumes = volumeRepo.readAll();
        int initialSize = volumes.size();

        Volume book1 = new Book(1,"Solaris","Scifi","Stanislaw Lem");
        Volume monthly1 = new Monthly(2, "Miesiecznik","Gatunek","Wydawca");

        volumeRepo.create(book1);
        volumeRepo.create(monthly1);

        volumes = volumeRepo.readAll();
        int finalSize = volumes.size();

        assertEquals(initialSize + 2, finalSize);
    }
}
