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

        TextField issuedBookIdField = new TextField();
        issuedBookIdField.setPromptText("Issued Book ID");

        DatePicker returnDatePicker = new DatePicker();

        Button returnButton = new Button("Return Book");
        returnButton.setOnAction(e -> {
            try {
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(
                        "UPDATE issued_books SET return_date = ? WHERE id = ?"
                );
                ps.setDate(1, java.sql.Date.valueOf(returnDatePicker.getValue()));
                ps.setInt(2, Integer.parseInt(issuedBookIdField.getText()));
                ps.executeUpdate();
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Book Returned!");
                alert.show();
                issuedBookIdField.clear();
            } catch (Exception ex) {
                ex.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR, "Error returning book!");
                alert.show();
            }
        });

        root.getChildren().addAll(new Label("Return Book"), issuedBookIdField, returnDatePicker, returnButton);

        stage.setScene(new Scene(root, 300, 200));
        stage.setTitle("Return Book");
        stage.show();
    }
}
