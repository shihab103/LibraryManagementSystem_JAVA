package com.example;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class ReturnBookPage {

    public void show() {
        Stage stage = new Stage();
        VBox root = new VBox(10);
        root.setStyle("-fx-padding: 20;");

        TextField issuedIdField = new TextField();
        issuedIdField.setPromptText("Issued Book ID");

        Button returnButton = new Button("Return Book");
        returnButton.setOnAction(e -> {
            try {
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(
                        "UPDATE issued_books SET return_date = CURDATE() WHERE id = ?"
                );
                ps.setInt(1, Integer.parseInt(issuedIdField.getText()));
                int rows = ps.executeUpdate();
                Alert alert;
                if(rows > 0) {
                    alert = new Alert(Alert.AlertType.INFORMATION, "Book Returned!");
                } else {
                    alert = new Alert(Alert.AlertType.WARNING, "No such issued book found!");
                }
                alert.show();
                issuedIdField.clear();
            } catch (Exception ex) {
                ex.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR, "Error returning book!");
                alert.show();
            }
        });

        root.getChildren().addAll(new Label("Return Book"), issuedIdField, returnButton);
        stage.setScene(new Scene(root, 300, 200));
        stage.setTitle("Return Book");
        stage.show();
    }

    // For Dashboard compatibility
    public void display() {
        show();
    }
}
