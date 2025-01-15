package repositories;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.core.type.DataTypes;
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder;
import dao.VolumeDao;
import mapper.VolumeMapper;
import mapper.VolumeMapperBuilder;
import model.Volume;

public class VolumeRepo {

    private final CqlSession session;
    private final VolumeMapper volumeMapper;
    private final VolumeDao volumeDao;

    public VolumeRepo(CqlSession session) {
        this.session = session;
        makeTable();
        this.volumeMapper = new VolumeMapperBuilder(session).build();
        this.volumeDao = volumeMapper.volumeDao();
    }

    public void makeTable() {
        SimpleStatement createVolumes =
                SchemaBuilder.createTable(CqlIdentifier.fromCql("volumes"))
                        .ifNotExists()
                        .withPartitionKey(CqlIdentifier.fromCql("volume_id"), DataTypes.BIGINT)
                        .withColumn("isRented", DataTypes.BOOLEAN)
                        .withColumn("title", DataTypes.TEXT)
                        .withColumn("genre", DataTypes.TEXT)
                        .build();
        session.execute(createVolumes);
    }

    public void create(Volume volume) {
        volumeDao.create(volume);
    }

    public Volume read(long volumeId) {
        return volumeDao.findById(volumeId);
    }

    public void update(Volume volume) {
        volumeDao.update(volume);
    }

    public void delete(long volumeId) {
        volumeDao.remove(volumeId);
    }
}