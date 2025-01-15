package mapper;

import com.datastax.oss.driver.api.mapper.annotations.*;
import dao.RenterDao;

@Mapper
public interface RenterMapper {
    @DaoFactory
    RenterDao renterDao(@DaoKeyspace String keyspace, @DaoTable String table);

    @DaoFactory
    RenterDao renterDao();
}