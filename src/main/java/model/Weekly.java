package model;

public class Weekly extends Publication {

    public Weekly(String title, String genre, String publisher) {
        super(title, genre, publisher);
    }

    @Override
    public String volumeInfo() {
        return "Publication: " + super.volumeInfo() + ", publisher: " + getPublisher();
    }
}
