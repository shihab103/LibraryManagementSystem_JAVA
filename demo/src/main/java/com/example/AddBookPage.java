package com.example;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AddBookPage {

    public void show() {
        Stage stage = new Stage();
        VBox root = new VBox(10);
        root.setStyle("-fx-padding: 20;");

        TextField titleField = new TextField();
        titleField.setPromptText("Book Title");

        TextField authorField = new TextField();
        authorField.setPromptText("Author");

        TextField isbnField = new TextField();
        isbnField.setPromptText("ISBN");

        Button addButton = new Button("Add Book");
        addButton.setOnAction(e -> {
            String title = titleField.getText();
            String author = authorField.getText();
            String isbn = isbnField.getText();

            // DAO লেয়ার ব্যবহার করে বই যোগ করা হচ্ছে
            boolean success = BookDAO.addBook(title, author, isbn);
            
            if (success) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "✅ Book Added Successfully!");
                alert.show();

                titleField.clear();
                authorField.clear();
                isbnField.clear();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "❌ Error adding book! Check database connection or fields.");
                alert.show();
            }
        });

        root.getChildren().addAll(new Label("Add New Book"), titleField, authorField, isbnField, addButton);
        stage.setScene(new Scene(root, 300, 250));
        stage.setTitle("Add Book");
        stage.show();
    }
}