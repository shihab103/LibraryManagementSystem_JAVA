package com.example;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.sql.Date; 

public class IssueBookPage {

    public void show() {
        Stage stage = new Stage();
        VBox root = new VBox(10);
        root.setStyle("-fx-padding: 20;");

        TextField userIdField = new TextField();
        userIdField.setPromptText("User ID");

        TextField bookIdField = new TextField();
        bookIdField.setPromptText("Book ID");

        Button issueButton = new Button("Issue Book");
        issueButton.setOnAction(e -> {
            try {
                int userId = Integer.parseInt(userIdField.getText());
                int bookId = Integer.parseInt(bookIdField.getText());

                // DAO লেয়ার ব্যবহার করে বই ইস্যু করা হচ্ছে
                boolean success = IssuedBookDAO.issueBook(userId, bookId, new Date(System.currentTimeMillis()));

                if (success) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "✅ Book Issued! Book marked as unavailable.");
                    alert.show();
                    userIdField.clear();
                    bookIdField.clear();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "❌ Error issuing book! Check User ID/Book ID.");
                    alert.show();
                }

            } catch (NumberFormatException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid ID format. Please enter numbers.");
                alert.show();
            } catch (Exception ex) {
                ex.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR, "An unexpected error occurred during issue!");
                alert.show();
            }
        });

        root.getChildren().addAll(new Label("Issue Book"), userIdField, bookIdField, issueButton);
        stage.setScene(new Scene(root, 300, 250));
        stage.setTitle("Issue Book");
        stage.show();
    }

    // For Dashboard compatibility
    public void display() {
        show();
    }
}