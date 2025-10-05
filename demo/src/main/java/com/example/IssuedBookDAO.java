package com.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class IssuedBookDAO {

    public static boolean issueBook(int userId, int bookId, Date issueDate) {
        try (Connection conn = DBConnection.getConnection()) {
            // 1. Insert into issued_books
            String sql = "INSERT INTO issued_books (user_id, book_id, issue_date) VALUES (?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.setInt(2, bookId);
            ps.setDate(3, issueDate);
            ps.executeUpdate();

            // 2. Mark book as unavailable
            String updateBook = "UPDATE books SET available = false WHERE id = ?";
            PreparedStatement ps2 = conn.prepareStatement(updateBook);
            ps2.setInt(1, bookId);
            ps2.executeUpdate();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean returnBook(int issuedId, Date returnDate) {
        try (Connection conn = DBConnection.getConnection()) {
            // 1. Update return_date
            String sql = "UPDATE issued_books SET return_date = ? WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setDate(1, returnDate);
            ps.setInt(2, issuedId);
            ps.executeUpdate();

            // 2. Mark book as available again
            String bookSql = "UPDATE books SET available = true WHERE id = (SELECT book_id FROM issued_books WHERE id = ?)";
            PreparedStatement ps2 = conn.prepareStatement(bookSql);
            ps2.setInt(1, issuedId);
            ps2.executeUpdate();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<String> getAllIssuedBooks() {
        List<String> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT ib.id, u.name, b.title, ib.issue_date, ib.return_date FROM issued_books ib " +
                    "JOIN users u ON ib.user_id = u.id " +
                    "JOIN books b ON ib.book_id = b.id";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(rs.getInt("id") + " | " + rs.getString("name") + " | " +
                        rs.getString("title") + " | Issue: " + rs.getDate("issue_date") +
                        " | Return: " + rs.getDate("return_date"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
