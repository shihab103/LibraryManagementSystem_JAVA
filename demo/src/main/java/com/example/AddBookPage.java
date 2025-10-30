package com.example;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;

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
            try (Connection conn = DBConnection.getConnection()) {
                PreparedStatement ps = conn.prepareStatement(
                        "INSERT INTO books(title, author, isbn) VALUES (?, ?, ?)"
                );
                ps.setString(1, titleField.getText());
                ps.setString(2, authorField.getText());
                ps.setString(3, isbnField.getText());
                ps.executeUpdate();

                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Book Added!");
                alert.show();

                titleField.clear();
                authorField.clear();
                isbnField.clear();
            } catch (Exception ex) {
                ex.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR, "Error adding book!");
                alert.show();
            }
        });

        root.getChildren().addAll(new Label("Add New Book"), titleField, authorField, isbnField, addButton);
        stage.setScene(new Scene(root, 300, 250));
        stage.setTitle("Add Book");
        stage.show();
    }
}
