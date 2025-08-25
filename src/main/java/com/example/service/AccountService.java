package com.example.service;

import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class AccountService {
    
    private static Set<String> activeUserCache;
    
    public static class Account {
        private String id;
        private String status;
        private User user;
        
        public Account(String id, String status, User user) {
            this.id = id;
            this.status = status;
            this.user = user;
        }
        
        public String getId() { return id; }
        public String getStatus() { return status; }
        public User getUser() { return user; }
        public boolean isActive() { return "active".equals(status); }
    }
    
    public static class User {
        private String name;
        private List<Email> emails;
        
        public User(String name, List<Email> emails) {
            this.name = name;
            this.emails = emails;
        }
        
        public String getName() { return name; }
        public List<Email> getEmails() { return emails; }
    }
    
    public static class Email {
        private String address;
        
        public Email(String address) {
            this.address = address;
        }
        
        public String getAddress() { return address; }
    }
    
    public int countActiveAccounts(List<Account> accounts) {
        int count = 0;
        for (int i = 0; i < accounts.size(); i++) {
            if (accounts.get(i).isActive()) {
                count++;
            }
        }
        return count;
    }
    
    public String getPrimaryEmail(User user) {
        // FIX: Added proper null and empty checks to prevent exceptions
        if (user.getEmails() != null && !user.getEmails().isEmpty()) {
            Email firstEmail = user.getEmails().get(0);
            if (firstEmail != null) {
                return firstEmail.getAddress();
            }
        }
        return "no-email@example.com";
    }
    
    public List<Account> findDuplicateAccounts(List<Account> accounts) {
        List<Account> duplicates = new ArrayList<>();
        Set<String> seen = new HashSet<>();
        
        for (Account account : accounts) {
            String key = account.getUser().getName();
            if (seen.contains(key)) {
                duplicates.add(account);
            } else {
                seen.add(key);
            }
        }
        return duplicates;
    }
    
    public String readTemplate(String templatePath) throws IOException {
        StringBuilder content = new StringBuilder();
        
        // FIX: Use try-with-resources to ensure BufferedReader is always closed
        try (BufferedReader reader = new BufferedReader(new FileReader(templatePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
                
                // Exception handling now safe - resource will be closed automatically
                if (line.contains("ERROR")) {
                    throw new IOException("Template contains error marker: " + line);
                }
                
                // Another exception path that is now safe
                if (content.length() > 10000) {
                    throw new IOException("Template file too large: " + content.length());
                }
            }
        } // BufferedReader and FileReader automatically closed here
        
        return content.toString();
    }
    
    public synchronized Set<String> getActiveUserCache() {
        if (activeUserCache == null) {
            activeUserCache = new HashSet<>();
            loadActiveUsers();
        }
        return activeUserCache;
    }
    
    private void loadActiveUsers() {
        activeUserCache.add("user1");
        activeUserCache.add("user2");
        activeUserCache.add("user3");
    }
}