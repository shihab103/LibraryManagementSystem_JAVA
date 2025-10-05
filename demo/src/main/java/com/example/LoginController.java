package com.example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.sql.*;

public class LoginController extends Application {

    @Override
    public void start(Stage stage) {
        Label title = new Label("📚 Library Management System");
        Label emailLabel = new Label("Email:");
        TextField emailField = new TextField();
        Label passLabel = new Label("Password:");
        PasswordField passField = new PasswordField();

        Button loginBtn = new Button("Login");
        Button registerBtn = new Button("Register");

        Label message = new Label();

        loginBtn.setOnAction(e -> {
            String email = emailField.getText();
            String password = passField.getText();

            if (loginUser(email, password)) {
                message.setText("✅ Login Successful!");
            } else {
                message.setText("❌ Invalid Credentials!");
            }
        });

        registerBtn.setOnAction(e -> {
            RegisterController register = new RegisterController();
            try {
                register.start(stage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        VBox layout = new VBox(10);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");
        layout.getChildren().addAll(title, emailLabel, emailField, passLabel, passField, loginBtn, registerBtn, message);

        Scene scene = new Scene(layout, 400, 300);
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
    }

    private boolean loginUser(String email, String password) {
        String query = "SELECT * FROM users WHERE email = ? AND password = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, email);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
