package com.example;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.sql.Date;

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
                int issuedId = Integer.parseInt(issuedIdField.getText());
                
                boolean success = IssuedBookDAO.returnBook(issuedId, new Date(System.currentTimeMillis()));

                if (success) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "✅ Book Returned! Book marked as available.");
                    alert.show();
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING, "⚠️ Return Failed! Check Issued Book ID.");
                    alert.show();
                }
                issuedIdField.clear();

            } catch (NumberFormatException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid ID format. Please enter numbers.");
                alert.show();
            } catch (Exception ex) {
                ex.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR, "An unexpected error occurred during return!");
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