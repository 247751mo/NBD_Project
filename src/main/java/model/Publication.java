package model;

import javax.persistence.*;
@Entity
@Table(name = "publications")
@Inheritance(strategy = InheritanceType.JOINED)
public class Publication extends Volume {

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
