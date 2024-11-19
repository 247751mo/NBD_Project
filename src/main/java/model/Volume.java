package model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.UUID;

@Getter
@NoArgsConstructor
@BsonDiscriminator(key = "type") // Określa klucz typu w przypadku dziedziczenia
public abstract class Volume {

    @BsonId // Oznacza pole jako unikalny identyfikator dla MongoDB
    private UUID volumeId;

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
    public Volume(@BsonProperty("title") String title,
                  @BsonProperty("genre") String genre) {
        if (title == null || title.isEmpty()) {
            throw new IllegalArgumentException("Invalid title (can't be empty)!");
        }
        if (genre == null || genre.isEmpty()) {
            throw new IllegalArgumentException("Invalid genre (can't be empty)!");
        }
        this.volumeId = UUID.randomUUID(); // Generuje nowe UUID
        this.title = title;
        this.genre = genre;
        this.isRented = false;
        this.isArchive = false;
    }

    public String volumeInfo() {
        return "Title: " + getTitle() + "\nGenre: " + getGenre();
    }
}
