package dao;


import com.datastax.oss.driver.api.mapper.annotations.Dao;
import com.datastax.oss.driver.api.mapper.annotations.QueryProvider;
import com.datastax.oss.driver.api.mapper.annotations.StatementAttributes;
import model.Renter;

@Dao
public class RenterDao {

    @StatementAttributes(consistencyLevel = "QUORUM")
    @QueryProvider(providerClass = RenterProvider.class)
    void create(Renter renter);

    @StatementAttributes(consistencyLevel = "ONE", pageSize = 100)
    @QueryProvider(providerClass = RenterProvider.class)
    Renter findById(long personalID);

    @StatementAttributes(consistencyLevel = "QUORUM")
    @QueryProvider(providerClass = RenterProvider.class)
    void update(Renter renter);

    @StatementAttributes(consistencyLevel = "QUORUM")
    @QueryProvider(providerClass = RenterProvider.class)
    void remove(long personalID);
}
