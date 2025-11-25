package com.example;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class ViewBooksPage {

    public Node getPane() {
        VBox root = new VBox(10);
        root.setStyle("-fx-padding: 20;");

        TableView<Book> table = new TableView<>();
        table.setPlaceholder(new Label("No books found in the library."));

        // Columns...
        TableColumn<Book, Number> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(data -> data.getValue().idProperty());
        idCol.setPrefWidth(60);

        TableColumn<Book, String> titleCol = new TableColumn<>("Title");
        titleCol.setCellValueFactory(data -> data.getValue().titleProperty());
        titleCol.setPrefWidth(200);

        TableColumn<Book, String> authorCol = new TableColumn<>("Author");
        authorCol.setCellValueFactory(data -> data.getValue().authorProperty());
        authorCol.setPrefWidth(200);

        TableColumn<Book, String> isbnCol = new TableColumn<>("ISBN");
        isbnCol.setCellValueFactory(data -> data.getValue().isbnProperty());
        isbnCol.setPrefWidth(150);

        table.getColumns().addAll(idCol, titleCol, authorCol, isbnCol);

        ObservableList<Book> data = FXCollections.observableArrayList();

        try (Connection conn = DBConnection.getConnection()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT id, title, author, isbn FROM books");

            while (rs.next()) {
                data.add(new Book(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("isbn")));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        table.setItems(data);

        // Make TableView take full height
        VBox.setVgrow(table, javafx.scene.layout.Priority.ALWAYS);

        root.getChildren().addAll(
                new Label("📖 All Books in Library"),
                table);

        return root;
    }

}
