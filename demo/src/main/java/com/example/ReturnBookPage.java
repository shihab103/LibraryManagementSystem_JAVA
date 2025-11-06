package com.example;

import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import java.sql.Date;

public class ReturnBookPage {

    public Node getPane() {
        VBox root = new VBox(10);
        root.setStyle("-fx-padding: 40; -fx-alignment: top_left; -fx-font-size: 16px;");

        TextField issuedIdField = new TextField();
        issuedIdField.setPromptText("Issued Book ID");
        issuedIdField.setMaxWidth(300);

        Button returnButton = new Button("Return Book");
        returnButton.setStyle("-fx-font-size: 14px; -fx-background-color: #3498db; -fx-text-fill: white;");

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

        root.getChildren().addAll(new Label("📥 Return Book"), issuedIdField, returnButton);
        return root;
    }
}