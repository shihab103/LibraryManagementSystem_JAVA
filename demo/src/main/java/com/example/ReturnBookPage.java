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
                
                double fine = IssuedBookDAO.returnBook(issuedId, new Date(System.currentTimeMillis()));

                if (fine >= 0) {
                    Alert alert;
                    if (fine > 0) {
                        alert = new Alert(Alert.AlertType.WARNING, 
                            String.format("বই সফলভাবে ফেরত নেওয়া হয়েছে, কিন্তু বিলম্বের জন্য %.2f টাকা ফাইন হয়েছে।", fine), 
                            ButtonType.OK);
                    } else {
                        alert = new Alert(Alert.AlertType.INFORMATION, "✅ বই সফলভাবে ফেরত নেওয়া হয়েছে! কোনো ফাইন নেই।");
                    }
                    alert.show();
                } else if (fine == -1) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "❌ ভুল ইস্যু আইডি। এই আইডি দিয়ে কোনো বই পাওয়া যায়নি বা ইতিমধ্যে ফেরত নেওয়া হয়েছে।");
                    alert.show();
                } else { 
                    Alert alert = new Alert(Alert.AlertType.ERROR, "❌ বই ফেরত নেওয়ার সময় অপ্রত্যাশিত ত্রুটি হয়েছে! ডাটাবেস চেক করুন।");
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