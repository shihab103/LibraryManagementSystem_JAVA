package com.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Date;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit; 

public class IssuedBookDAO {

    public static boolean issueBook(int userId, int bookId, Date issueDate) {
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false); 
            
            java.util.Calendar cal = java.util.Calendar.getInstance();
            cal.setTime(issueDate);
            cal.add(java.util.Calendar.DATE, 7); 
            Date dueDate = new Date(cal.getTimeInMillis()); 


            String sql = "INSERT INTO issued_books (user_id, book_id, issue_date, due_date) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.setInt(2, bookId);
            ps.setDate(3, issueDate);
            ps.setDate(4, dueDate); 
            ps.executeUpdate();

            String updateBook = "UPDATE books SET available = false WHERE id = ?";
            PreparedStatement ps2 = conn.prepareStatement(updateBook);
            ps2.setInt(1, bookId);
            ps2.executeUpdate();
            
            conn.commit(); 
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public static double returnBook(int issuedId, Date returnDate) {
        double fineAmount = 0.00;
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false); 
            String getDatesSql = "SELECT book_id, due_date FROM issued_books WHERE id = ? AND return_date IS NULL";
            PreparedStatement psGet = conn.prepareStatement(getDatesSql);
            psGet.setInt(1, issuedId);
            ResultSet rs = psGet.executeQuery();
            
            if (rs.next()) {
                int bookId = rs.getInt("book_id");
                Date dueDate = rs.getDate("due_date");
                
                if (returnDate.after(dueDate)) {
                    long diff = returnDate.getTime() - dueDate.getTime();
                    long diffDays = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
                    
                    fineAmount = diffDays * 20.00; 
                }


                String sql = "UPDATE issued_books SET return_date = ?, fine = ? WHERE id = ?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setDate(1, returnDate);
                ps.setDouble(2, fineAmount);
                ps.setInt(3, issuedId);
                ps.executeUpdate();

                String bookSql = "UPDATE books SET available = true WHERE id = ?";
                PreparedStatement ps2 = conn.prepareStatement(bookSql);
                ps2.setInt(1, bookId);
                ps2.executeUpdate();

                conn.commit();
                return fineAmount; 
            }
            return -1; 
        } catch (Exception e) {
            e.printStackTrace();
            return -2; 
        }
    }

    public static List<String> getAllIssuedBooks() {
        List<String> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT ib.id, u.name, b.title, ib.issue_date, ib.due_date FROM issued_books ib " +
                    "JOIN users u ON ib.user_id = u.id " +
                    "JOIN books b ON ib.book_id = b.id WHERE ib.return_date IS NULL";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add("ID: " + rs.getInt("id") + " | User: " + rs.getString("name") + " | Book: " +
                        rs.getString("title") + " | Issue: " + rs.getDate("issue_date") + " | Due: " + rs.getDate("due_date"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    
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