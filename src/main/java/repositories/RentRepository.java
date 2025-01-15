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
        SimpleStatement createRents =
                SchemaBuilder.createTable(CqlIdentifier.fromCql("rent_a_volume"))
                        .ifNotExists()
                        .withPartitionKey(CqlIdentifier.fromCql("rent_id"), DataTypes.BIGINT)
                        .withColumn("volume_id", DataTypes.BIGINT)
                        .withColumn("personal_id", DataTypes.BIGINT)
                        .withColumn("start_date", DataTypes.TEXT)
                        .build();
                session.execute(createRents);
    }

    public void create(Rent rent) {
        rentDao.create(rent);
    }

    public void update(Rent rent) {
        rentDao.update(rent);
    }

    public Rent findbyID(long rentID){
        return rentDao.findById(rentID);
    }

    public void remove(Rent rent) {
        rentDao.remove(rent);
    }
}