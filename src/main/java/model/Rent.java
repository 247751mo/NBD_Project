package model;
import jakarta.persistence.*;
import exceptions.ParameterException;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "rents")
@Access(AccessType.FIELD)
public class Rent {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "renter_id", nullable = false)
    private Renter renter;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "volume_id", nullable = false)
    private Volume volume; // Volume type

    @Column(name = "BeginTime")
    private LocalDateTime beginTime;
    @Column(name = "EndTime")
    private LocalDateTime endTime;

    public Rent(UUID id, Renter renter, Volume volume, LocalDateTime beginTime) {
        this.id = id;
        this.renter = renter;
        this.volume = volume;
        this.beginTime = beginTime;

        if (beginTime == null) {
            this.beginTime = LocalDateTime.now();
        }

        this.endTime = null;

        if (renter == null) {
            throw new ParameterException("Cannot create rent without renter!");
        }

        if (volume == null) {
            throw new ParameterException("Cannot create rent without volume!");
        }
    }

    public Rent() {

    }

    public UUID getId() {
        return id;
    }

    public Renter getRenter() {
        return renter;
    }

    public Volume getVolume() {
        return volume;
    }

    public LocalDateTime getBeginTime() {
        return beginTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public String getInfo() {
        return String.format("(Rent) rent id: %s %s %s begin time: %s, end time: %s",
                id, renter.getInfo(), volume.volumeInfo(), beginTime, endTime);
    }

    public void endRent(LocalDateTime argEndTime) {
        if (endTime == null) {
            if (argEndTime == null) {
                this.endTime = LocalDateTime.now();
            } else {
                if (argEndTime.isAfter(beginTime)) {
                    this.endTime = argEndTime;
                } else {
                    this.endTime = beginTime;
                }
            }
        }
    }

    public void setVolume(Volume volume) {
        this.volume = volume;
    }
}
