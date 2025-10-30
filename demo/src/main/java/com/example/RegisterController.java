package com.example;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import org.mindrot.jbcrypt.BCrypt;

public class RegisterController {

    public void start(Stage stage) {
        Label title = new Label("🆕 Register New Account");
        Label nameLabel = new Label("Full Name:");
        TextField nameField = new TextField();
        Label emailLabel = new Label("Email:");
        TextField emailField = new TextField();
        Label passLabel = new Label("Password:");
        PasswordField passField = new PasswordField();

        Button registerBtn = new Button("Register");
        Button backBtn = new Button("Back to Login");
        Label message = new Label();

        registerBtn.setOnAction(e -> {
            String name = nameField.getText();
            String email = emailField.getText();
            String password = passField.getText();

            if (registerUser(name, email, password)) {
                message.setText("✅ Registration Successful!");
            } else {
                message.setText("❌ Registration Failed!");
            }
        });

        backBtn.setOnAction(e -> {
            LoginController login = new LoginController();
            login.start(stage);
        });

        VBox layout = new VBox(10);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");
        layout.getChildren().addAll(title, nameLabel, nameField, emailLabel, emailField, passLabel, passField, registerBtn, backBtn, message);

        Scene scene = new Scene(layout, 400, 350);
        stage.setTitle("Register");
        stage.setScene(scene);
        stage.show();
    }

    private boolean registerUser(String name, String email, String password) {
        String hashed = BCrypt.hashpw(password, BCrypt.gensalt());
        String query = "INSERT INTO users (name, email, password) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, hashed);

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
