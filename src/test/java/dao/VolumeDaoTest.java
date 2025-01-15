package dao;


import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.mapper.annotations.Dao;
import com.datastax.oss.driver.api.mapper.annotations.QueryProvider;
import com.datastax.oss.driver.api.mapper.annotations.StatementAttributes;
import model.Book;
import model.Publication;
import model.Renter;
import model.Volume;
import provider.VolumeProvider;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import repositories.CassandraRepository;

import static org.junit.jupiter.api.Assertions.*;

class VolumeDaoTest {
    private static VolumeDao volumeDao;
    private static CqlSession session;

    @BeforeAll
    static void setUp() {
        CassandraRepository cassandraRepository = new CassandraRepository();
        cassandraRepository.initSession();
        session = cassandraRepository.getSession();
        volumeDao = new VolumeMapperBuilder(session).build().VolumeDao();
    }

    @Test
    void addBook(){
        Book book = new Book(1, "Book", "W pustyni i w puszczy", "Henryk Sienkiewicz", "Powiesc");
        volumeDao.create(book);
    }
}


