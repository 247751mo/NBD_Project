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
import com.datastax.oss.driver.api.querybuilder.update.Update;
import model.Rent;
import model.Renter;

import java.util.List;

import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.literal;

public class RentProvider {
    private final CqlSession session;

    public static final CqlIdentifier RENT_A_VOLUME = CqlIdentifier.fromCql("rent_a_volume");
    public static final CqlIdentifier RENTS_BY_RENTER = CqlIdentifier.fromCql("rents_by_renter");
    public static final CqlIdentifier RENTS_BY_VOLUME = CqlIdentifier.fromCql("rents_by_volume");
    public static final CqlIdentifier RENT_ID = CqlIdentifier.fromCql("rent_id");
    public static final CqlIdentifier START_DATE = CqlIdentifier.fromCql("start_date");
    public static final CqlIdentifier PERSONAL_ID = CqlIdentifier.fromCql("personal_id");
    public static final CqlIdentifier VOLUME_ID = CqlIdentifier.fromCql("volume_id");


    public RentProvider(MapperContext ctx) {
        this.session = ctx.getSession();
    }

    public void create(Rent rent) {
        Insert insertRenter = QueryBuilder.insertInto(RENTS_BY_RENTER)
                .value(PERSONAL_ID, literal(rent.getPersonalID()))
                .value(RENT_ID, literal(rent.getRentID()))
                .value(START_DATE, literal(rent.getStartDate()))
                .value(VOLUME_ID, literal(rent.getVolumeID()))
                .ifNotExists();

        Insert insertVolume = QueryBuilder.insertInto(RENTS_BY_VOLUME)
                .value(VOLUME_ID, literal(rent.getVolumeID()))
                .value(RENT_ID, literal(rent.getRentID()))
                .value(START_DATE, literal(rent.getStartDate()))
                .value(PERSONAL_ID, literal(rent.getPersonalID()))
                .ifNotExists();

        session.execute(insertRenter.build());
        session.execute(insertVolume.build());
    }

    public Rent findByRenterId(long personalID) {
        Select selectRenter = QueryBuilder.selectFrom(RENTS_BY_RENTER)
                .all()
                .where(Relation.column(PERSONAL_ID).isEqualTo(QueryBuilder.literal(personalID)));
        ResultSet resultSet = session.execute(selectRenter.build());
        Row row = resultSet.one();

        if (row == null) {
            return null;
        }

        return new Rent(
                row.getLong(RENT_ID),
                row.getString(START_DATE),
                row.getLong(VOLUME_ID),
                row.getLong(PERSONAL_ID)
        );
    }

    public Rent findByVolumeId(long volumeID) {
        Select selectVolume = QueryBuilder.selectFrom(RENTS_BY_VOLUME)
                .all()
                .where(Relation.column(VOLUME_ID).isEqualTo(QueryBuilder.literal(volumeID)));
        ResultSet resultSet = session.execute(selectVolume.build());
        Row row = resultSet.one();

        if (row == null) {
            return null;
        }

        return new Rent(
                row.getLong(RENT_ID),
                row.getString(START_DATE),
                row.getLong(PERSONAL_ID),
                row.getLong(VOLUME_ID)
        );
    }

    public void update(Rent rent) {
        Update updateRenter = QueryBuilder.update(RENTS_BY_RENTER)
                .setColumn(START_DATE, QueryBuilder.literal(rent.getStartDate()))
                .where(Relation.column(PERSONAL_ID).isEqualTo(QueryBuilder.literal(rent.getPersonalID())));
        session.execute(updateRenter.build());

        Update updateVolume = QueryBuilder.update(RENTS_BY_VOLUME)
                .setColumn(START_DATE, QueryBuilder.literal(rent.getStartDate()))
                .where(Relation.column(VOLUME_ID).isEqualTo(QueryBuilder.literal(rent.getVolumeID())));
        session.execute(updateVolume.build());
    }

    public void remove(Rent rent) {
        Delete deleteRenter = QueryBuilder.deleteFrom(RENTS_BY_RENTER)
                .where(Relation.column(PERSONAL_ID).isEqualTo(literal(rent.getPersonalID())))
                .where(Relation.column(RENT_ID).isEqualTo(literal(rent.getRentID())));

        Delete deleteVolume = QueryBuilder.deleteFrom(RENTS_BY_VOLUME)
                .where(Relation.column(VOLUME_ID).isEqualTo(literal(rent.getVolumeID())))
                 .where(Relation.column(RENT_ID).isEqualTo(literal(rent.getRentID())));

        BatchStatement batchStatement = BatchStatement.builder(BatchType.LOGGED)
                .addStatement(deleteRenter.build())
                .addStatement(deleteVolume.build())
                .build();

        session.execute(batchStatement);
    }

}

