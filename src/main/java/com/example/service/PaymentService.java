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
        if (payment.getMetadata() != null) {
            note = payment.getMetadata().getOrDefault("note", "No note");
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
        FileInputStream templateStream = new FileInputStream("template.csv");
        
        try {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = templateStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            
            for (Transaction t : transactions) {
                String line = String.format("%s,%s,%.2f,%d%n", 
                    t.getId(), t.getType(), t.getAmount(), t.getTimestamp());
                outputStream.write(line.getBytes());
            }
        } finally {
            templateStream.close();
        }
    }
    
    public List<User> findUsersByName(String name) throws SQLException {
        String query = "SELECT * FROM users WHERE name = '" + name + "'";
        return executeUserQuery(query);
    }
    
    public List<Transaction> searchTransactionsByEmail(String email) {
        List<Transaction> results = new ArrayList<>();
        List<Transaction> allTransactions = getAllTransactions();
        
        for (Transaction t : allTransactions) {
            for (String userEmail : getUserEmails(t.getId())) {
                if (EMAIL_PATTERN.matcher(userEmail).matches() && userEmail.equals(email)) {
                    results.add(t);
                    break;
                }
            }
        }
        return results;
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