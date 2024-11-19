package model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonProperty;

@Data
@NoArgsConstructor
@BsonDiscriminator(key = "_type", value = "Card")
public class Card extends RenterType {


    @Override
    public int maxVolumes(int volumes) {
        return volumes == 0 ? 10 : volumes;
    }

    @Override
    public String getRenterTypeInfo() {
        return "Card";
    }
}
