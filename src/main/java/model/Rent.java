package model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Getter
@NoArgsConstructor
@JsonTypeInfo(
        use = JsonTypeInfo.Id.CLASS,
        include = JsonTypeInfo.As.PROPERTY,
        property = "@class"
)
public class Rent {

    @BsonId
    private UUID id;

    @BsonProperty("renter")
    @Setter
    private Renter renter;

    @BsonProperty("volume")
    @Setter
    private Volume volume;

    @BsonProperty("beginTime")
    @Setter
    private LocalDateTime beginTime;

    @BsonProperty("endTime")
    @Setter
    private LocalDateTime endTime;

    @BsonCreator
    public Rent(@BsonProperty("id") @JsonProperty("id") UUID id,
                @BsonProperty("renter") @JsonProperty("renter") Renter renter,
                @BsonProperty("volume") @JsonProperty("volume") Volume volume,
                @BsonProperty("beginTime") @JsonProperty("beginTime") LocalDateTime beginTime) {
        this.id = id;
        this.renter = renter;
        this.volume = volume;
        this.beginTime = beginTime != null ? beginTime : LocalDateTime.now();
    }

    public Rent(Renter renter, Volume volume, LocalDateTime beginTime) {
        this.id = java.util.UUID.randomUUID();
        this.renter = renter;
        this.volume = volume;
        this.beginTime = beginTime != null ? beginTime.truncatedTo(ChronoUnit.SECONDS) : LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
    }


}
