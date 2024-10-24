package model;

import jakarta.persistence.*;


@Entity
@DiscriminatorValue("Publication")
@Access(AccessType.FIELD)
public class Publication extends Volume {

    @Column(nullable = true)
    private String publisher;

    public Publication(String title, String genre, String publisher) {
        super(title, genre);
        this.publisher = publisher;
    }

    public Publication() {

    }

    public String getPublisher() {
        return publisher;
    }

    @Override
    public String volumeInfo() {
        return super.volumeInfo();
    }
}
