package model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;

@Data
@NoArgsConstructor
@BsonDiscriminator(key = "_type", value = "Weekly")
public class Weekly extends Publication {

    @BsonCreator
    public Weekly(@BsonId String volumeId,
                @BsonProperty("title") String title,
                  @BsonProperty("genre") String genre,
                  @BsonProperty("publisher") String publisher) {
        super(title, genre, publisher);
    }
    public Weekly(String title, String genre, String publisher) {
        this(null, title, genre, publisher);
    }
}
