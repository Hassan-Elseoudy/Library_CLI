package io.hassan;

public class Book {

    private Integer id;
    private String title;
    private String author;
    private String description;

    public Book(Integer id, String title, String author, String description) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.description = description;
    }

    public Book(String title, String author, String description){
        this.title = title;
        this.author = author;
        this.description = description;
    }

    /* Decorating */

    @Override
    public String toString() {
        return String.format("ID: %d, Title: %s, Author: %s, Description: %s", this.getId(), this.getTitle(), this.getAuthor(), this.getDescription());
    }

    public String toStringSummary() {
        return String.format("[%d] %s", this.getId(), this.getTitle());
    }

    public String toStringCSV() {
        return this.getId() +
                "," + this.getTitle() +
                "," + this.getAuthor() +
                "," + this.getDescription();

    }

    /* GETTERS & SETTERS */

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
