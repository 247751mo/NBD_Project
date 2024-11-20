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
@BsonDiscriminator(key = "_type", value = "Publication")
public class Publication extends Volume {

    @BsonProperty("publisher")
    private String publisher;

    @BsonCreator
    public Publication(@BsonId Integer volumeId,
                        @BsonProperty("title") String title,
                       @BsonProperty("genre") String genre,
                       @BsonProperty("publisher") String publisher) {
        super(volumeId, title, genre);
        this.publisher = publisher;
    }

}
