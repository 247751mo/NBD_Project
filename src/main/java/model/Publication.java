package model;

public class Publication extends Volume {

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
