package model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonProperty;

@Data
@NoArgsConstructor
@BsonDiscriminator(key = "_type", value = "NoCard")
public class NoCard extends RenterType {

    @BsonCreator
    public NoCard() {
        // Domyślny konstruktor
    }

    @Override
    public int maxVolumes(int volumes) {
        return volumes == 0 ? 5 : volumes;
    }

    @Override
    public String getRenterTypeInfo() {
        return "No Card";
    }
}
