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
}


