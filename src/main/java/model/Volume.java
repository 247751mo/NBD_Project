package model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.Objects;
import java.util.UUID;

@Getter
@NoArgsConstructor
@BsonDiscriminator(key = "_type", value = "volume")
public abstract class Volume {

    @BsonId
    private Integer volumeId;

    @BsonProperty("title")
    @Setter
    private String title;

    @BsonProperty("genre")
    @Setter
    private String genre;

    @BsonProperty("isRented")
    @Setter
    private int isRented;

    @BsonProperty("isArchive")
    @Setter
    private boolean isArchive;

    @BsonCreator
    public Volume(@BsonProperty("volumeId") Integer volumeId,
                  @BsonProperty("title") String title,
                  @BsonProperty("genre") String genre) {

        if (volumeId == null) {
            throw new IllegalArgumentException("Volume ID cannot be null!");
        }
        if (title == null || title.isEmpty()) {
            throw new IllegalArgumentException("Invalid title (can't be empty)!");
        }
        if (genre == null || genre.isEmpty()) {
            throw new IllegalArgumentException("Invalid genre (can't be empty)!");
        }
        this.volumeId = volumeId;
        this.title = title;
        this.genre = genre;
        this.isRented = 0;
        this.isArchive = false;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Volume volume = (Volume) o;
        return isRented == volume.isRented &&
                isArchive == volume.isArchive &&
                Objects.equals(volumeId, volume.volumeId) &&
                Objects.equals(title, volume.title) &&
                Objects.equals(genre, volume.genre);


    }


    @Override
    public int hashCode() {
       return Objects.hash(volumeId,genre,title);
    }
}
