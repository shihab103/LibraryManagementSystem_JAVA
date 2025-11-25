package com.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Date;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit; // ফাইন হিসাবের জন্য

public class IssuedBookDAO {

    /**
     * একটি নির্দিষ্ট ইউজারকে বই ইস্যু করে, ৭ দিনের জন্য 'due_date' সেট করে এবং বইটিকে 'unavailable' করে দেয়।
     */
    public static boolean issueBook(int userId, int bookId, Date issueDate) {
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false); // ট্রানজ্যাকশন শুরু
            
            // Due Date ক্যালকুলেশন (ইস্যু ডেট + 7 দিন)
            java.util.Calendar cal = java.util.Calendar.getInstance();
            cal.setTime(issueDate);
            cal.add(java.util.Calendar.DATE, 7); // 7 দিন যোগ করা হলো
            Date dueDate = new Date(cal.getTimeInMillis()); // নতুন ডিউ ডেট

            // 1. Insert into issued_books (due_date কলাম যুক্ত)
            // ধরে নেওয়া হলো issued_books টেবিলে due_date কলাম যোগ করা হয়েছে
            String sql = "INSERT INTO issued_books (user_id, book_id, issue_date, due_date) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.setInt(2, bookId);
            ps.setDate(3, issueDate);
            ps.setDate(4, dueDate); // ডিউ ডেট সেট
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
     * বই ফেরত নেয়, ফাইন হিসাব করে এবং বইটিকে পুনরায় 'available' করে দেয়।
     * রিটার্ন ভ্যালু হিসেবে ফাইন এমাউন্ট (double) রিটার্ন করে।
     */
    public static double returnBook(int issuedId, Date returnDate) {
        double fineAmount = 0.00;
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false); // ট্রানজ্যাকশন শুরু

            // 1. ডিউ ডেট এবং book_id পুনরুদ্ধার করা
            String getDatesSql = "SELECT book_id, due_date FROM issued_books WHERE id = ? AND return_date IS NULL";
            PreparedStatement psGet = conn.prepareStatement(getDatesSql);
            psGet.setInt(1, issuedId);
            ResultSet rs = psGet.executeQuery();
            
            if (rs.next()) {
                int bookId = rs.getInt("book_id");
                Date dueDate = rs.getDate("due_date");
                
                // ফাইন হিসাব: ডিউ ডেট পার হলে প্রতিদিন 20 টাকা ফাইন
                if (returnDate.after(dueDate)) {
                    long diff = returnDate.getTime() - dueDate.getTime();
                    // দিন সংখ্যা বের করা
                    long diffDays = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
                    
                    fineAmount = diffDays * 20.00; // প্রতিদিন 20 টাকা ফাইন
                }

                // 2. issued_books আপডেট: return_date এবং fine কলাম আপডেট
                // ধরে নেওয়া হলো issued_books টেবিলে fine কলাম যোগ করা হয়েছে
                String sql = "UPDATE issued_books SET return_date = ?, fine = ? WHERE id = ?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setDate(1, returnDate);
                ps.setDouble(2, fineAmount);
                ps.setInt(3, issuedId);
                ps.executeUpdate();

                // 3. Mark book as available again
                String bookSql = "UPDATE books SET available = true WHERE id = ?";
                PreparedStatement ps2 = conn.prepareStatement(bookSql);
                ps2.setInt(1, bookId);
                ps2.executeUpdate();

                conn.commit(); // ট্রানজ্যাকশন শেষ
                return fineAmount; // ফাইন এমাউন্ট রিটার্ন করা হলো
            }
            return -1; // যদি issuedId না পাওয়া যায় বা বইটি ইতিমধ্যে ফেরত দেওয়া হয়ে থাকে
        } catch (Exception e) {
            e.printStackTrace();
            return -2; // অন্য কোনো ত্রুটি বোঝানোর জন্য
        }
    }

    /**
     * বর্তমানে ইস্যু হওয়া সব বইয়ের তালিকা নিয়ে আসে (এখন due date সহ)।
     */
    public static List<String> getAllIssuedBooks() {
        List<String> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection()) {
            // SQL: due_date কলাম যোগ করা হয়েছে
            String sql = "SELECT ib.id, u.name, b.title, ib.issue_date, ib.due_date FROM issued_books ib " +
                    "JOIN users u ON ib.user_id = u.id " +
                    "JOIN books b ON ib.book_id = b.id WHERE ib.return_date IS NULL";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                // ডিসপ্লেতে due_date যোগ করা হয়েছে
                list.add("ID: " + rs.getInt("id") + " | User: " + rs.getString("name") + " | Book: " +
                        rs.getString("title") + " | Issue: " + rs.getDate("issue_date") + " | Due: " + rs.getDate("due_date"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    
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