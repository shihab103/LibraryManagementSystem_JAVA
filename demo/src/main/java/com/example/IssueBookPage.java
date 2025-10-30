package com.example;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.PreparedStatement;

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
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(
                        "INSERT INTO issued_books(user_id, book_id, issue_date) VALUES (?, ?, CURDATE())"
                );
                ps.setInt(1, Integer.parseInt(userIdField.getText()));
                ps.setInt(2, Integer.parseInt(bookIdField.getText()));
                ps.executeUpdate();
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Book Issued!");
                alert.show();
                userIdField.clear();
                bookIdField.clear();
            } catch (Exception ex) {
                ex.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR, "Error issuing book!");
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
