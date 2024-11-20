package model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;

@Data
@NoArgsConstructor
@BsonDiscriminator(key = "_type", value = "Monthly")
public class Monthly extends Publication {

    @BsonCreator
    public Monthly(@BsonId Integer volumeId,
                   @BsonProperty("title") String title,
                   @BsonProperty("genre") String genre,
                   @BsonProperty("publisher") String publisher) {
        super(volumeId, title, genre, publisher);
    }
}

