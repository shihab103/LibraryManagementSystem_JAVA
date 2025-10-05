package com.example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.sql.Connection;

public class Main extends Application {
    @Override
    public void start(Stage stage) {
        Label statusLabel = new Label("Welcome to Library Management System");
        Button connectBtn = new Button("Connect to Database");

        connectBtn.setOnAction(e -> {
            Connection conn = DBConnection.getConnection();
            if (conn != null) {
                statusLabel.setText("✅ Connected to Database!");
            } else {
                statusLabel.setText("❌ Failed to Connect!");
            }
        });

        VBox layout = new VBox(15);
        layout.getChildren().addAll(statusLabel, connectBtn);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        Scene scene = new Scene(layout, 400, 200);
        stage.setTitle("Library Management System");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
