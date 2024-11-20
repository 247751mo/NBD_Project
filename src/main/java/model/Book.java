package model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;

@Getter
@Setter
@NoArgsConstructor
@BsonDiscriminator(key = "_type", value = "Book")
public class Book extends Volume {

    @BsonProperty("author")
    private String author;

    @BsonCreator
    public Book(@BsonId String volumeId,
                @BsonProperty("title") String title,
                @BsonProperty("genre") String genre,
                @BsonProperty("author") String author) {
        super(null, title, genre);
        this.author = author;
    }
    public Book(String title, String genre, String author) {
        this(null, title, genre, author);
    }
}
