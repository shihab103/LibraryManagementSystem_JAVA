package com.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Date;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class IssuedBookDAO {

    /**
     * একটি নির্দিষ্ট ইউজারকে বই ইস্যু করে এবং বইটিকে 'unavailable' করে দেয়।
     */
    public static boolean issueBook(int userId, int bookId, Date issueDate) {
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false); // ট্রানজ্যাকশন শুরু

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
            
            conn.commit(); // ট্রানজ্যাকশন শেষ
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * বই ফেরত নেয় এবং বইটিকে পুনরায় 'available' করে দেয়।
     */
    public static boolean returnBook(int issuedId, Date returnDate) {
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false); // ট্রানজ্যাকশন শুরু

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

            conn.commit(); // ট্রানজ্যাকশন শেষ
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * বর্তমানে ইস্যু হওয়া সব বইয়ের তালিকা নিয়ে আসে।
     */
    public static List<String> getAllIssuedBooks() {
        List<String> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT ib.id, u.name, b.title, ib.issue_date, ib.return_date FROM issued_books ib " +
                    "JOIN users u ON ib.user_id = u.id " +
                    "JOIN books b ON ib.book_id = b.id WHERE ib.return_date IS NULL";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add("ID: " + rs.getInt("id") + " | User: " + rs.getString("name") + " | Book: " +
                        rs.getString("title") + " | Issue Date: " + rs.getDate("issue_date"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    
    // --- ড্যাশবোর্ড স্ট্যাটিস্টিকসের জন্য নতুন মেথড ---

    /**
     * বর্তমানে ইস্যু হয়ে আছে কিন্তু ফেরত আসেনি এমন বইয়ের সংখ্যা গণনা করে।
     */
    public static int countIssuedBooks() {
        int count = 0;
        String query = "SELECT COUNT(*) FROM issued_books WHERE return_date IS NULL";
        
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