package com.example;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Book {
    private final StringProperty title;
    private final StringProperty author;
    private final StringProperty isbn;

    public Book(String title, String author, String isbn) {
        this.title = new SimpleStringProperty(title);
        this.author = new SimpleStringProperty(author);
        this.isbn = new SimpleStringProperty(isbn);
    }

    public StringProperty titleProperty() { return title; }
    public StringProperty authorProperty() { return author; }
    public StringProperty isbnProperty() { return isbn; }
}
