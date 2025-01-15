package provider;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.BatchStatement;
import com.datastax.oss.driver.api.core.cql.BatchType;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.mapper.MapperContext;
import com.datastax.oss.driver.api.querybuilder.QueryBuilder;
import com.datastax.oss.driver.api.querybuilder.delete.Delete;
import com.datastax.oss.driver.api.querybuilder.insert.Insert;
import com.datastax.oss.driver.api.querybuilder.relation.Relation;
import com.datastax.oss.driver.api.querybuilder.select.Select;
import model.Rent;
import model.Renter;

import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.literal;

public class RentProvider {
    private final CqlSession session;

    public static final CqlIdentifier RENT_A_VOLUME = CqlIdentifier.fromCql("rent_a_volume");
    public static final CqlIdentifier RENT_ID = CqlIdentifier.fromCql("rent_id");
    public static final CqlIdentifier START_DATE = CqlIdentifier.fromCql("start_date");
    public static final CqlIdentifier PERSONAL_ID = CqlIdentifier.fromCql("personal_id");
    public static final CqlIdentifier VOLUME_ID = CqlIdentifier.fromCql("volume_id");


    public RentProvider(MapperContext ctx) {
        this.session = ctx.getSession();
    }

    public void create(Rent rent) {
        Insert insertRent = QueryBuilder.insertInto(RENT_A_VOLUME)
                .value(RENT_ID, literal(rent.getRentID()))
                .value(START_DATE, literal(rent.getStartDate()))
                .value(VOLUME_ID, literal(rent.getVolumeID()))
                .value(PERSONAL_ID, literal(rent.getPersonalID()))
                .ifNotExists();

        session.execute(insertRent.build());
    }



    public void remove(Rent rent) {
        Delete deleteRent = QueryBuilder.deleteFrom(RENT_A_VOLUME)
                .where(Relation.column(RENT_ID).isEqualTo(literal(rent.getRentID())));


        BatchStatement batchStatement = BatchStatement.builder(BatchType.LOGGED)
                .addStatement(deleteRent.build())
                .build();

        session.execute(batchStatement);
    }

}

