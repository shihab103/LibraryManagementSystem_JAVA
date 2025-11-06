package com.example;

import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class AddBookPage {

    public Node getPane() {
        VBox root = new VBox(10);
        root.setStyle("-fx-padding: 40; -fx-alignment: top_left; -fx-font-size: 16px;");

        TextField titleField = new TextField();
        titleField.setPromptText("Book Title");
        titleField.setMaxWidth(300);

        TextField authorField = new TextField();
        authorField.setPromptText("Author");
        authorField.setMaxWidth(300);

        TextField isbnField = new TextField();
        isbnField.setPromptText("ISBN");
        isbnField.setMaxWidth(300);

        Button addButton = new Button("Add Book");
        addButton.setStyle("-fx-font-size: 14px; -fx-background-color: #2ecc71; -fx-text-fill: white;");

        addButton.setOnAction(e -> {
            String title = titleField.getText();
            String author = authorField.getText();
            String isbn = isbnField.getText();

            boolean success = BookDAO.addBook(title, author, isbn);
            
            if (success) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "✅ Book Added Successfully!", ButtonType.OK);
                alert.show();

                titleField.clear();
                authorField.clear();
                isbnField.clear();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "❌ Error adding book! Check database connection or fields.");
                alert.show();
            }
        });

        root.getChildren().addAll(new Label("📚 Add New Book"), titleField, authorField, isbnField, addButton);
        return root;
    }
}