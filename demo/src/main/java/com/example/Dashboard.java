package com.example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Dashboard extends Application {

    @Override
    public void start(Stage stage) {
        stage.setTitle("📚 Library Management System - Dashboard");

        Button addBookBtn = new Button("Add Book");
        Button viewBooksBtn = new Button("View Books");
        Button issueBookBtn = new Button("Issue Book");
        Button returnBookBtn = new Button("Return Book");
        Button logoutBtn = new Button("Logout");

        addBookBtn.setOnAction(e -> AddBookPage.display());
        viewBooksBtn.setOnAction(e -> ViewBooksPage.display());
        issueBookBtn.setOnAction(e -> IssueBookPage.display());
        returnBookBtn.setOnAction(e -> ReturnBookPage.display());
        logoutBtn.setOnAction(e -> {
            stage.close();
            Main.main(null); // Back to Login page
        });

        VBox layout = new VBox(15);
        layout.getChildren().addAll(addBookBtn, viewBooksBtn, issueBookBtn, returnBookBtn, logoutBtn);

        Scene scene = new Scene(layout, 300, 300);
        stage.setScene(scene);
        stage.show();
    }

    public static void display() {
        Application.launch();
    }
}
