package model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonProperty;

@Getter
@Setter
@NoArgsConstructor
@BsonDiscriminator(key = "_type", value = "Publication")
public class Publication extends Volume {

    @BsonProperty("publisher")
    private String publisher;

    @BsonCreator
    public Publication(@BsonProperty("title") String title,
                       @BsonProperty("genre") String genre,
                       @BsonProperty("publisher") String publisher) {
        super(title, genre);
        this.publisher = publisher;
    }

    @Override
    public String volumeInfo() {
        return super.volumeInfo() + ", publisher: " + publisher;
    }
}
