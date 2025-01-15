package provider;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.mapper.MapperContext;
import com.datastax.oss.driver.api.mapper.entity.EntityHelper;
import com.datastax.oss.driver.api.querybuilder.QueryBuilder;
import com.datastax.oss.driver.api.querybuilder.delete.Delete;
import com.datastax.oss.driver.api.querybuilder.relation.Relation;
import com.datastax.oss.driver.api.querybuilder.select.Select;
import model.Book;
import model.Publication;
import model.Volume;

public class VolumeProvider {
    private final CqlSession session;
    private final EntityHelper<Book> bookHelper;
    private final EntityHelper<Publication> publicationHelper;

    public VolumeProvider(MapperContext ctx, EntityHelper<Book> bookHelper, EntityHelper<Publication> publicationHelper) {
        this.session = ctx.getSession();
        this.bookHelper = bookHelper;
        this.publicationHelper = publicationHelper;
    }

    public void create(Volume volume) {
        session.execute(
                switch (volume.getDiscriminator()) {
                    case "book" -> {
                        Book book = (Book) volume;
                        yield session.prepare(bookHelper.insert().build())
                                .bind()
                                .setLong("volume_id", book.getVolumeId())
                                .setString("discriminator", book.getDiscriminator())
                                .setString("title", book.getTitle())
                                .setString("author", book.getAuthor())
                                .setString("genre", book.getGenre())
                                .setBoolean("isRented", book.isRented());
                    }
                    case "publication" -> {
                        Publication publication = (Publication) volume;
                        yield session.prepare(publicationHelper.insert().build())
                                .bind()
                                .setLong("volume_id", publication.getVolumeId())
                                .setString("discriminator", publication.getDiscriminator())
                                .setString("title", publication.getTitle())
                                .setString("publisher", publication.getPublisher())
                                .setString("genre", publication.getGenre())
                                .setBoolean("isRented", publication.isRented());
                    }
                    default -> throw new IllegalArgumentException();
                }
        );
    }

    public Volume findById(long volumeID) {
        Select selectVolume = (Select) QueryBuilder.selectFrom(CqlIdentifier.fromCql("volumes"))
                .all()
                .where(Relation.column(CqlIdentifier.fromCql("volume_id")).isEqualTo(QueryBuilder.literal(volumeID)));
        try {
            Row row = session.execute(selectVolume.build()).one();
            String discriminator = row.getString("discriminator");
            return switch (discriminator) {
                case "book" -> getBook(row);
                case "publication" -> getPublication(row);
                default -> throw new IllegalArgumentException();
            };
        } catch (NullPointerException e) {
            return null;
        }
    }
    private Book getBook(Row row) {
    return new Book(
            row.getLong("volume_id"),
            row.getString("discriminator"),
            row.getString("title"),
            row.getString("author"),
            row.getString("genre")
    );

    }
    private Publication getPublication(Row row) {
        return new Publication(
                row.getLong("volume_id"),
                row.getString("discriminator"),
                row.getString("title"),
                row.getString("publisher"),
                row.getString("genre")
        );
    }

    public void update(Volume volume) {
        try {
            session.execute(
                    switch (volume.getDiscriminator()) {
                        case "book" -> {
                            Book book = (Book) volume;
                            yield session.prepare(bookHelper.updateByPrimaryKey().build())
                                    .bind()
                                    .setLong("volume_id", book.getVolumeId())
                                    .setString("title", book.getTitle())
                                    .setString("author", book.getAuthor())
                                    .setString("genre", book.getGenre())
                                    .setBoolean("isRented", book.isRented());
                        }
                        case "publication" -> {
                            Publication publication = (Publication) volume;
                            yield session.prepare(publicationHelper.updateByPrimaryKey().build())
                                    .bind()
                                    .setLong("volume_id", publication.getVolumeId())
                                    .setString("title", publication.getTitle())
                                    .setString("publisher", publication.getPublisher())
                                    .setString("genre", publication.getGenre())
                                    .setBoolean("isRented", publication.isRented());
                        }
                        default -> throw new IllegalArgumentException();
                    }
            );
        } catch (NullPointerException e) {
            System.out.println("Volume does not exist");
        }
    }

    public void remove(long volumeID) {
        Delete deleteVolume = QueryBuilder.deleteFrom(CqlIdentifier.fromCql("volumes"))
                .where(Relation.column(CqlIdentifier.fromCql("volume_id")).isEqualTo(QueryBuilder.literal(volumeID)));
        session.execute(deleteVolume.build());
    }

}

