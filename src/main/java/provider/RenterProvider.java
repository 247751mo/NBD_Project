package provider;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.mapper.MapperContext;
import com.datastax.oss.driver.api.querybuilder.QueryBuilder;
import com.datastax.oss.driver.api.querybuilder.delete.Delete;
import com.datastax.oss.driver.api.querybuilder.insert.Insert;
import com.datastax.oss.driver.api.querybuilder.relation.Relation;
import com.datastax.oss.driver.api.querybuilder.select.Select;
import com.datastax.oss.driver.api.querybuilder.update.Update;
import model.Renter;


public class RenterProvider {
    private final CqlSession session;

    public static final CqlIdentifier RENTERS = CqlIdentifier.fromCql("renters");
    public static final CqlIdentifier PERSONAL_ID = CqlIdentifier.fromCql("personal_id");
    public static final CqlIdentifier FIRST_NAME = CqlIdentifier.fromCql("first_name");
    public static final CqlIdentifier LAST_NAME = CqlIdentifier.fromCql("last_name");

    public RenterProvider(MapperContext ctx) {
        this.session = ctx.getSession();
    }

    public void create(Renter renter) {
        Insert insertRenter = QueryBuilder.insertInto(RENTERS)
                .value(PERSONAL_ID, QueryBuilder.literal(renter.getPersonalId()))
                .value(FIRST_NAME, QueryBuilder.literal(renter.getFirstName()))
                .value(LAST_NAME, QueryBuilder.literal(renter.getLastName()))
                .ifNotExists();

        session.execute(insertRenter.build());
    }

    public Renter findById(long personalID) {
        Select selectRenter = QueryBuilder.selectFrom(RENTERS)
                .all()
                .where(Relation.column(PERSONAL_ID).isEqualTo(QueryBuilder.literal(personalID)));
        ResultSet resultSet = session.execute(selectRenter.build());
        Row row = resultSet.one();

        if (row == null) {
            return null;
        }

        return new Renter(
                row.getLong(PERSONAL_ID),
                row.getString(FIRST_NAME),
                row.getString(LAST_NAME)
        );
    }

    public void update(Renter renter) {
        Update updateRenter = QueryBuilder.update(RENTERS)
                .setColumn(FIRST_NAME, QueryBuilder.literal(renter.getFirstName()))
                .setColumn(LAST_NAME, QueryBuilder.literal(renter.getLastName()))
                .where(Relation.column(PERSONAL_ID).isEqualTo(QueryBuilder.literal(renter.getPersonalId())));
        session.execute(updateRenter.build());
    }

    public void remove(long personalID) {
        Delete deleteRenter = QueryBuilder.deleteFrom(RENTERS)
                .where(Relation.column(PERSONAL_ID).isEqualTo(QueryBuilder.literal(personalID)));

        session.execute(deleteRenter.build());
    }

}
