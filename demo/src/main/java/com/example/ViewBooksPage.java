package com.example;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class ViewBooksPage {

    public void show() {
        Stage stage = new Stage();
        VBox root = new VBox(10);
        root.setStyle("-fx-padding: 20;");

        TableView<Book> table = new TableView<>();
        TableColumn<Book, String> titleCol = new TableColumn<>("Title");
        titleCol.setCellValueFactory(data -> data.getValue().titleProperty());

        TableColumn<Book, String> authorCol = new TableColumn<>("Author");
        authorCol.setCellValueFactory(data -> data.getValue().authorProperty());

        TableColumn<Book, String> isbnCol = new TableColumn<>("ISBN");
        isbnCol.setCellValueFactory(data -> data.getValue().isbnProperty());

        table.getColumns().addAll(titleCol, authorCol, isbnCol);

        ObservableList<Book> data = FXCollections.observableArrayList();

        try (Connection conn = DBConnection.getConnection()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM books");
            while (rs.next()) {
                data.add(new Book(rs.getString("title"), rs.getString("author"), rs.getString("isbn")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        table.setItems(data);
        root.getChildren().addAll(new Label("All Books"), table);

        stage.setScene(new Scene(root, 400, 300));
        stage.setTitle("View Books");
        stage.show();
    }
}
