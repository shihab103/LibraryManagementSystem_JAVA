package com.example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.mindrot.jbcrypt.BCrypt;

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
                Dashboard dashboard = new Dashboard();
                dashboard.start(stage);
            } else {
                message.setText("❌ Invalid Credentials!");
            }
        });

        registerBtn.setOnAction(e -> {
            RegisterController register = new RegisterController();
            register.start(stage);
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
        String query = "SELECT password FROM users WHERE email = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String hashed = rs.getString("password");
                return BCrypt.checkpw(password, hashed);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
