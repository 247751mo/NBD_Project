package dao;

import com.datastax.oss.driver.api.mapper.annotations.Dao;
import com.datastax.oss.driver.api.mapper.annotations.QueryProvider;
import com.datastax.oss.driver.api.mapper.annotations.StatementAttributes;
import model.Rent;
import provider.RentProvider;

@Dao
public interface RentDao {
    @StatementAttributes(consistencyLevel = "QUORUM")
    @QueryProvider(providerClass = RentProvider.class)
    void create(Rent rent);

    @StatementAttributes(consistencyLevel = "QUORUM")
    @QueryProvider(providerClass = RentProvider.class)
    void update(Rent rent);

    @StatementAttributes(consistencyLevel = "ONE", pageSize = 100)
    @QueryProvider(providerClass = RentProvider.class)
    Rent findById (long rentID);

    @StatementAttributes(consistencyLevel = "QUORUM")
    @QueryProvider(providerClass = RentProvider.class)
    void remove(Rent rent);

}
