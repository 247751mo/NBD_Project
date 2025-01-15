package repositories;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.core.type.DataTypes;
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder;
import dao.RenterDao;
import mapper.RenterMapper;
import model.Renter;


public class RenterRepo {

    private final CqlSession session;
    private final RenterMapper renterMapper;
    private final RenterDao renterDao;

    public RenterRepo(CqlSession session) {
        this.session = session;
        makeTable();
        this.renterMapper = new RenterMapperBuilder(session).build();
        this.renterDao = renterMapper.renterDao();
    }

    public void makeTable() {
        SimpleStatement createRenters =
                SchemaBuilder.createTable(CqlIdentifier.fromCql("renters"))
                        .ifNotExists()
                        .withPartitionKey(CqlIdentifier.fromCql("personal_id"), DataTypes.TEXT)
                        .withColumn("type", DataTypes.TEXT)
                        .withColumn("first_name", DataTypes.TEXT)
                        .withColumn("last_name", DataTypes.TEXT)
                        .build();
        session.execute(createRenters);
    }

    public void create(Renter renter) {
        renterDao.create(renter);
    }

    public Renter read(long personalID) {
        return renterDao.findById(personalID);
    }

    public void update(Renter renter) {
        renterDao.update(renter);
    }

    public void delete(long personalID) {
        renterDao.remove(personalID);
    }
}