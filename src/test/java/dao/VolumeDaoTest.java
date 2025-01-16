package dao;


import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.mapper.annotations.Dao;
import com.datastax.oss.driver.api.mapper.annotations.QueryProvider;
import com.datastax.oss.driver.api.mapper.annotations.StatementAttributes;
import mapper.VolumeMapperBuilder;
import model.Book;
import model.Publication;
import model.Renter;
import model.Volume;
import org.junit.jupiter.api.Assertions;
import provider.VolumeProvider;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import repositories.CassandraRepository;
import repositories.VolumeRepo;

import static org.junit.jupiter.api.Assertions.*;

class VolumeDaoTest {
    private static VolumeDao volumeDao;
    private static VolumeRepo volumeRepo;
    private static CqlSession session;

    @BeforeAll
    static void setUp() {
        CassandraRepository cassandraRepository = new CassandraRepository();
        cassandraRepository.initSession();
        session = cassandraRepository.getSession();
        volumeRepo = new VolumeRepo(session);
        volumeDao = new VolumeMapperBuilder(session).build().volumeDao();
    }

    @Test
    void addBook(){
        Book book = new Book(1, "book", "W pustyni i w puszczy", "Powiesc", "Henryk Sienkiewicz", false);
        volumeDao.create(book);
    }

    @Test
    void addPublication(){
        Publication publication = new Publication(2, "publication", "Bravo Sport", "Sport", "Niedziela", false);
        volumeDao.create(publication);
    }

    @Test
    void findById() {
        Volume volume = volumeDao.findById(1);
        System.out.println(volume);
        Assertions.assertEquals("W pustyni i w puszczy", volume.getTitle());
    }
    @Test
    void delete() {
        Book book = new Book(32, "book","Book1", "BookGenre", "BookAuthor", false);
        volumeDao.create(book);
        Volume volume2 = volumeDao.findById(32);
        assertNotNull(volume2);
        volumeDao.remove(32);
        Volume volume = volumeDao.findById(32);
        assertNull(volume);
        System.out.println(volume);
    }

    @Test
    void update() {
        Book book = new Book(33, "book", "Book1", "BookGenre", "BookAuthor", false);
        volumeDao.create(book);
        book.setTitle("Book2");
        volumeDao.update(book);
        Volume retrievedVolume = volumeDao.findById(33);
        assertEquals("Book2", retrievedVolume.getTitle());
    }
}


