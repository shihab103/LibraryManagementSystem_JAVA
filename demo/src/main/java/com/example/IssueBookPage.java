package com.example;

import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import java.sql.Date; 

public class IssueBookPage {

    public Node getPane() {
        VBox root = new VBox(10);
        root.setStyle("-fx-padding: 40; -fx-alignment: top_left; -fx-font-size: 16px;");

        TextField userIdField = new TextField();
        userIdField.setPromptText("User ID");
        userIdField.setMaxWidth(300);

        TextField bookIdField = new TextField();
        bookIdField.setPromptText("Book ID");
        bookIdField.setMaxWidth(300);

        Button issueButton = new Button("Issue Book");
        issueButton.setStyle("-fx-font-size: 14px; -fx-background-color: #f39c12; -fx-text-fill: white;");

        issueButton.setOnAction(e -> {
            try {
                int userId = Integer.parseInt(userIdField.getText());
                int bookId = Integer.parseInt(bookIdField.getText());

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

        root.getChildren().addAll(new Label("📤 Issue Book to User"), userIdField, bookIdField, issueButton);
        return root;
    }
}