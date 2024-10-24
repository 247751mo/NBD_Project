package model;

import jakarta.persistence.*;

@Entity
@Access(AccessType.FIELD)
@DiscriminatorValue("Book")
public class Book extends Volume {

    @Column(nullable = true)
    private String author;

    public Book(String author, String title, String genre) {
        super(title, genre);
        this.author = author;
    }

    public Book() {

    }

    public String getAuthor() {
        return author;
    }

    @Override
    public String volumeInfo() {
        return "Book: " + super.volumeInfo() + ", author: " + getAuthor();
    }
}

