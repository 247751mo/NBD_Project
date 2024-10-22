package model;
import javax.persistence.*;

@Entity
@Access(AccessType.FIELD)
@DiscriminatorValue("book")
public class Book extends Volume {

    @Column(nullable = false)
    private String author;

    public Book(String author, String title, String genre) {
        super(title, genre);
        this.author = author;
    }

    public String getAuthor() {
        return author;
    }

    @Override
    public String volumeInfo() {
        return "Book: " + super.volumeInfo() + ", author: " + getAuthor();
    }
}

