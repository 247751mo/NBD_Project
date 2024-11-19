package model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Getter
@Setter
@NoArgsConstructor
public class Rent {

    @BsonId
    private String id;

    @BsonProperty("renter")
    private Renter renter;

    @BsonProperty("volume")
    private Volume volume;

    @BsonProperty("beginTime")
    private LocalDateTime beginTime;

    @BsonProperty("endTime")
    private LocalDateTime endTime;

    @BsonCreator
    public Rent(@BsonProperty("id") String id,
                @BsonProperty("renter") Renter renter,
                @BsonProperty("volume") Volume volume,
                @BsonProperty("beginTime") LocalDateTime beginTime) {
        this.id = id;
        this.renter = renter;
        this.volume = volume;
        this.beginTime = beginTime != null ? beginTime : LocalDateTime.now();
    }

    public Rent(Renter renter, Volume volume, LocalDateTime beginTime) {
        this.id = java.util.UUID.randomUUID().toString(); // Automatyczne generowanie ID
        this.renter = renter;
        this.volume = volume;
        this.beginTime = beginTime != null ? beginTime.truncatedTo(ChronoUnit.SECONDS) : LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
    }

    @BsonIgnore
    public void endRent()
    {
        setEndTime(LocalDateTime.now());
    }

    @BsonIgnore
    public long getRentDuration() {
        return java.time.Duration.between(beginTime, endTime).toDays();
    }
}
