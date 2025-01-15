package mapper;

import com.datastax.oss.driver.api.mapper.annotations.*;
import dao.VolumeDao;

@Mapper
public interface VolumeMapper {
    @DaoFactory
    VolumeDao volumeDao(@DaoKeyspace String keyspace, @DaoTable String table);

    @DaoFactory
    VolumeDao volumeDao();
}