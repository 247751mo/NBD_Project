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
@BsonDiscriminator(key = "_type", value = "Volume") // Określa klucz typu w przypadku dziedziczenia
public abstract class Volume {

    @BsonId // Oznacza pole jako unikalny identyfikator dla MongoDB
    private String volumeId;

    @BsonProperty("title")
    @Setter
    private String title;

    @BsonProperty("genre")
    @Setter
    private String genre;

    @BsonProperty("isRented")
    @Setter
    private boolean isRented;

    @BsonProperty("isArchive")
    @Setter
    private boolean isArchive;

    @BsonCreator
    public Volume(@BsonProperty("volumeId") String volumeId,
                  @BsonProperty("title") String title,
                  @BsonProperty("genre") String genre) {
        this.volumeId = (volumeId != null && !volumeId.isEmpty()) ? volumeId : UUID.randomUUID().toString();

        if (title == null || title.isEmpty()) {
            throw new IllegalArgumentException("Invalid title (can't be empty)!");
        }
        if (genre == null || genre.isEmpty()) {
            throw new IllegalArgumentException("Invalid genre (can't be empty)!");
        }
        this.volumeId = UUID.randomUUID().toString();
        this.title = title;
        this.genre = genre;
        this.isRented = false;
        this.isArchive = false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Volume volume = (Volume) o;

        return isRented == volume.isRented &&
                isArchive == volume.isArchive &&
                volumeId.equals(volume.volumeId) &&
                title.equals(volume.title) &&
                genre.equals(volume.genre);
    }

    @Override
    public int hashCode() {
       return Objects.hash(volumeId, isRented, isArchive, genre,title);
    }
}
