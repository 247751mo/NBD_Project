package dao;


import com.datastax.oss.driver.api.mapper.annotations.Dao;
import com.datastax.oss.driver.api.mapper.annotations.QueryProvider;
import com.datastax.oss.driver.api.mapper.annotations.StatementAttributes;
import model.Book;
import model.Publication;
import model.Renter;
import model.Volume;
import provider.VolumeProvider;

@Dao
public interface VolumeDao {

    @StatementAttributes(consistencyLevel = "QUORUM")
    @QueryProvider(providerClass = VolumeProvider.class, entityHelpers = {Book.class, Publication.class})
    void create(Volume volume);

    @StatementAttributes(consistencyLevel = "ONE", pageSize = 100)
    @QueryProvider(providerClass = VolumeProvider.class, entityHelpers = {Book.class, Publication.class})
    Volume findById(long volumeId);

    @StatementAttributes(consistencyLevel = "QUORUM")
    @QueryProvider(providerClass = VolumeProvider.class, entityHelpers = {Book.class, Publication.class})
    void update(Volume volume);

    @StatementAttributes(consistencyLevel = "QUORUM")
    @QueryProvider(providerClass = VolumeProvider.class, entityHelpers = {Book.class, Publication.class})
    void remove(long volumeId);
}
