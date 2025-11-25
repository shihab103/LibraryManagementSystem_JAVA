package com.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
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

    public static int countTotalBooks() {
        return executeCountQuery("SELECT COUNT(*) FROM books");
    }

    public static int countAvailableBooks() {
        return executeCountQuery("SELECT COUNT(*) FROM books WHERE available = true");
    }

    public static int countTotalUsers() {
        return executeCountQuery("SELECT COUNT(*) FROM users");
    }

    private static int executeCountQuery(String query) {
        int count = 0;
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }
}