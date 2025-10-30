package com.example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Dashboard extends Application {

    @Override
    public void start(Stage stage) {
        VBox root = new VBox(10);
        root.setStyle("-fx-padding: 20; -fx-alignment: center;");

        Button addBookBtn = new Button("Add Book");
        addBookBtn.setOnAction(e -> new AddBookPage().show());

        Button viewBooksBtn = new Button("View Books");
        viewBooksBtn.setOnAction(e -> new ViewBooksPage().show());

        Button logoutBtn = new Button("Logout");
        logoutBtn.setOnAction(e -> {
            LoginController login = new LoginController();
            login.start(stage);
        });

        root.getChildren().addAll(addBookBtn, viewBooksBtn, logoutBtn);

        Scene scene = new Scene(root, 300, 200);
        stage.setTitle("Library Dashboard");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
