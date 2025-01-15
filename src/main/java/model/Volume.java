package model;

import exceptions.ParameterException;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
public abstract class Volume implements Serializable {
    @Version
    private long version;
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID volumeId;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String genre;
    @Column(name = "is_rented")
    private boolean isRented;
    @Column(name = "is_archive")
    private boolean isArchive;
    public Volume(){

    }
    public Volume(String title, String genre) throws ParameterException {
        this.title = title;
        this.genre = genre;
        this.isArchive = false;

        if (title == null || title.isEmpty()) {
            throw new ParameterException("Invalid title (can't be empty)!");
        }

        if (genre == null || genre.isEmpty()) {
            throw new ParameterException("Invalid genre (can't be empty)!");
        }
    }


}
