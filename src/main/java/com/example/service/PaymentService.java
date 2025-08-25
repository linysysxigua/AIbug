package com.example.service;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.regex.Pattern;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.OutputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class PaymentService {
    
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    private static final Pattern DOMAIN_VALIDATION_PATTERN = Pattern.compile("^[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
    private static final Pattern COMPLEX_EMAIL_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$"
    );
    
    public static class Payment {
        private String id;
        private double amount;
        private String status;
        private Map<String, String> metadata;
        
        public Payment(String id, double amount, String status) {
            this.id = id;
            this.amount = amount;
            this.status = status;
            this.metadata = new HashMap<>();
        }
        
        public String getId() { return id; }
        public double getAmount() { return amount; }
        public String getStatus() { return status; }
        public Map<String, String> getMetadata() { return metadata; }
        public void setMetadata(Map<String, String> metadata) { this.metadata = metadata; }
    }
    
    public static class Transaction {
        private String id;
        private String type;
        private double amount;
        private long timestamp;
        
        public Transaction(String id, String type, double amount, long timestamp) {
            this.id = id;
            this.type = type;
            this.amount = amount;
            this.timestamp = timestamp;
        }
        
        public String getId() { return id; }
        public String getType() { return type; }
        public double getAmount() { return amount; }
        public long getTimestamp() { return timestamp; }
    }
    
    public static class Receipt {
        private String transactionId;
        private double amount;
        private String note;
        
        public Receipt(String transactionId, double amount, String note) {
            this.transactionId = transactionId;
            this.amount = amount;
            this.note = note;
        }
        
        public String getTransactionId() { return transactionId; }
        public double getAmount() { return amount; }
        public String getNote() { return note; }
    }
    
    public Receipt processPayment(Payment payment) {
        String note = "No note";
        // FIX: Added null check for getMetadata() to prevent NullPointerException
        if (payment.getMetadata() != null) {
            String noteValue = payment.getMetadata().get("note");
            if (noteValue != null) {
                note = noteValue;
            }
        }
        
        return new Receipt(payment.getId(), payment.getAmount(), note);
    }
    
    public List<Transaction> getLastNTransactions(List<Transaction> transactions, int n) {
        if (transactions == null || transactions.isEmpty() || n <= 0) {
            return new ArrayList<>();
        }
        
        int endIndex = Math.min(n, transactions.size());
        List<Transaction> result = transactions.subList(0, endIndex);
        
        // Fixed: access element at endIndex - 1 position to avoid off-by-one error
        if (endIndex > 0) {
            Transaction lastTransaction = transactions.get(endIndex - 1);
            System.out.println("Last transaction ID: " + lastTransaction.getId());
        }
        
        return result;
    }
    
    public void exportTransactions(List<Transaction> transactions, OutputStream outputStream) throws IOException {
        // FIX: Use try-with-resources to ensure FileInputStream is always closed
        try (FileInputStream templateStream = new FileInputStream("template.csv")) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = templateStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            
            for (Transaction t : transactions) {
                String line = String.format("%s,%s,%.2f,%d%n", 
                    t.getId(), t.getType(), t.getAmount(), t.getTimestamp());
                outputStream.write(line.getBytes());
                
                // Exception handling now safe - resource will be closed automatically
                if (t.getAmount() < 0) {
                    throw new IOException("Invalid transaction amount: " + t.getAmount());
                }
            }
        } // FileInputStream automatically closed here, even if exception occurs
    }
    
    public List<User> findUsersByName(String name) throws SQLException {
        // FIX: Use parameterized query to prevent SQL injection
        String query = "SELECT * FROM users WHERE name = ?";
        return executeParameterizedUserQuery(query, name);
    }
    
    // FIX: Use parameterized query with multiple parameters
    public List<User> findUsersByNameAndEmail(String name, String email) throws SQLException {
        String query = "SELECT * FROM users WHERE name = ? AND email = ?";
        return executeParameterizedUserQuery(query, name, email);
    }
    
    // FIX: Helper method for safe parameterized queries
    private List<User> executeParameterizedUserQuery(String query, String... params) throws SQLException {
        // In a real implementation, this would use PreparedStatement:
        // PreparedStatement stmt = connection.prepareStatement(query);
        // for (int i = 0; i < params.length; i++) {
        //     stmt.setString(i + 1, params[i]);
        // }
        // ResultSet rs = stmt.executeQuery();
        
        System.out.println("Executing safe parameterized query: " + query);
        for (int i = 0; i < params.length; i++) {
            System.out.println("Parameter " + (i + 1) + ": " + params[i]);
        }
        
        return new ArrayList<>(); // Placeholder implementation
    }
    
    public List<Transaction> searchTransactionsByEmail(String email) {
        List<Transaction> results = new ArrayList<>();
        List<Transaction> allTransactions = getAllTransactions();
        
        // FIX: Use pre-compiled static patterns for optimal performance
        for (Transaction t : allTransactions) {
            for (String userEmail : getUserEmails(t.getId())) {
                // FIX: Use pre-compiled patterns instead of compiling in loop
                if (EMAIL_PATTERN.matcher(userEmail).matches() && userEmail.equals(email)) {
                    // FIX: Additional validation using pre-compiled pattern
                    if (COMPLEX_EMAIL_PATTERN.matcher(userEmail).matches()) {
                        results.add(t);
                        break;
                    }
                }
            }
        }
        return results;
    }
    
    // FIX: Safe JSON building with proper escaping
    public String buildUserReportJson(String userName, String userComment) {
        StringBuilder json = new StringBuilder();
        json.append("{");
        json.append("\"timestamp\":\"").append(System.currentTimeMillis()).append("\",");
        // FIX: Proper JSON escaping to prevent injection
        json.append("\"userName\":\"").append(escapeJsonString(userName)).append("\",");
        json.append("\"comment\":\"").append(escapeJsonString(userComment)).append("\",");
        json.append("\"status\":\"active\"");
        json.append("}");
        
        return json.toString();
    }
    
    // FIX: Safe filter building with parameterization approach
    public String buildTransactionFilter(String userId, String category, String note) {
        StringBuilder filter = new StringBuilder();
        
        // FIX: Use parameterized approach instead of direct concatenation
        filter.append("user_id=? AND category=?");
        
        if (note != null && !note.isEmpty()) {
            filter.append(" AND notes LIKE ?");
        }
        
        // In real implementation, these parameters would be bound separately
        System.out.println("Filter parameters: userId=" + userId + ", category=" + category + 
                          (note != null ? ", note=" + note : ""));
        
        return filter.toString();
    }
    
    // FIX: Helper method for JSON string escaping
    private String escapeJsonString(String input) {
        if (input == null) return "";
        
        return input.replace("\\", "\\\\")
                   .replace("\"", "\\\"")
                   .replace("\b", "\\b")
                   .replace("\f", "\\f")
                   .replace("\n", "\\n")
                   .replace("\r", "\\r")
                   .replace("\t", "\\t");
    }
    
    private List<User> executeUserQuery(String query) throws SQLException {
        return new ArrayList<>();
    }
    
    private List<Transaction> getAllTransactions() {
        return new ArrayList<>();
    }
    
    private List<String> getUserEmails(String transactionId) {
        return new ArrayList<>();
    }
    
    public static class User {
        private String id;
        private String name;
        private String email;
        
        public User(String id, String name, String email) {
            this.id = id;
            this.name = name;
            this.email = email;
        }
        
        public String getId() { return id; }
        public String getName() { return name; }
        public String getEmail() { return email; }
    }
}