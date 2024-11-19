package model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;

@Getter
@NoArgsConstructor
//@BsonDiscriminator(key = "_type", value = "RenterType")
public abstract class RenterType {

    public abstract int maxVolumes(int volumes);

    public abstract String getRenterTypeInfo();
}
