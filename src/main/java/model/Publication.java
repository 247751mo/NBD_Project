package model;

import javax.persistence.*;


@MappedSuperclass
@Access(AccessType.FIELD)
public abstract class Publication extends Volume {

    @Column(nullable = false)
    private String publisher;

    public Publication(String title, String genre, String publisher) {
        super(title, genre);
        this.publisher = publisher;
    }

    public String getPublisher() {
        return publisher;
    }

    @Override
    public String volumeInfo() {
        return super.volumeInfo();
    }
}
