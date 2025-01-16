package repositories;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.core.type.DataTypes;
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder;
import dao.RentDao;
import dao.RenterDao;
import mapper.RentMapper;
import mapper.RentMapperBuilder;
import model.Rent;
import model.Renter;

public class RentRepository {

    private final CqlSession session;
    private final RentMapper rentMapper;
    private final RentDao rentDao;

    public RentRepository(CqlSession session) {
        this.session = session;
        makeTable();
        this.rentMapper = new RentMapperBuilder(session).build();
        this.rentDao = rentMapper.rentDao();
    }

    private void makeTable() {
        SimpleStatement createRentsByRenter =
                SchemaBuilder.createTable(CqlIdentifier.fromCql("rents_by_renter"))
                        .ifNotExists()
                        .withPartitionKey(CqlIdentifier.fromCql("personal_id"), DataTypes.BIGINT)
                        .withClusteringColumn(CqlIdentifier.fromCql("rent_id"), DataTypes.BIGINT)
                        .withColumn("volume_id", DataTypes.BIGINT)
                        .withColumn("start_date", DataTypes.TEXT)
                        .build();
                session.execute(createRentsByRenter);

        SimpleStatement createRentsByVolume =
                SchemaBuilder.createTable(CqlIdentifier.fromCql("rents_by_volume"))
                        .ifNotExists()
                        .withPartitionKey(CqlIdentifier.fromCql("volume_id"), DataTypes.BIGINT)
                        .withClusteringColumn(CqlIdentifier.fromCql("rent_id"), DataTypes.BIGINT)
                        .withColumn("personal_id", DataTypes.BIGINT)
                        .withColumn("start_date", DataTypes.TEXT)
                        .build();
        session.execute(createRentsByRenter);
    }

    public void create(Rent rent) {
        rentDao.create(rent);
    }

    public void update(Rent rent) {
        rentDao.update(rent);
    }

    public Rent findByRenterID(long personalID) {
        return rentDao.findByRenterId(personalID);
    }

    public Rent findByVolumeID(long volumeID) {
        return rentDao.findByVolumeId(volumeID);
    }

    public void remove(Rent rent) {
        rentDao.remove(rent);
    }
}