package com.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {

    public static boolean addBook(String title, String author, String isbn) {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "INSERT INTO books (title, author, isbn, available) VALUES (?, ?, ?, true)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, title);
            ps.setString(2, author);
            ps.setString(3, isbn);
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<String> getAllBooks() {
        List<String> books = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT id, title, author, isbn, available FROM books";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                books.add(rs.getInt("id") + " | " + rs.getString("title") + " | " + rs.getString("author") + " | ISBN: " + rs.getString("isbn") + " | Available: " + rs.getBoolean("available"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return books;
    }
}
