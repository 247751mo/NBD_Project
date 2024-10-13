package model;

import exceptions.ParameterException;

public class Volume {

    private String title;
    private String genre;
    private boolean isRented;
    private boolean isArchive;

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

    public String getTitle() {
        return title;
    }

    public String getGenre() {
        return genre;
    }

    public boolean checkIfRented() {
        return isRented;
    }

    public boolean checkIfArchived() {
        return isArchive;
    }

    public void setTitle(String newTitle) {
        if (newTitle != null && !newTitle.isEmpty()) {
            this.title = newTitle;
        }
    }

    public void setGenre(String newGenre) {
        if (newGenre != null && !newGenre.isEmpty()) {
            this.genre = newGenre;
        }
    }

    public void setRentedStatus(boolean isRented) {
        this.isRented = isRented;
    }

    public void setArchiveStatus(boolean isArchive) {
        if (this.isArchive != isArchive) {
            this.isArchive = isArchive;
        }
    }

    public String volumeInfo() {
        return "Title: " + getTitle() + "\nGenre: " + getGenre();
    }
}
