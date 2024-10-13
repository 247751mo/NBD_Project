package model;

public class Book extends Volume {

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

