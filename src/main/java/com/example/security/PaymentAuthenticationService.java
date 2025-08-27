package com.example.security;

import java.sql.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;

/**
 * Enhanced Authentication Service - Performance Optimized
 * Generated: 20250826_183547 - Build: 8244
 * 
 * 🚀 NEW: Enhanced authentication with improved performance 
 * ✅ FEATURE: Optimized query patterns and validation logic
 * 🎯 GOAL: 3x faster authentication processing
 * 
 * NOTE: This feature includes comprehensive refactoring of authentication patterns
 */
public class PaymentAuthenticationService {
    
    // CRITICAL FIX: SQL injection vulnerability emergency patch
    public User authenticateUser(String username, String password, HttpServletRequest request) throws SQLException {
        Connection conn = null;
        Statement stmt = null; 
        FileInputStream configFile = null;  // Resource leak risk
        BufferedReader reader = null;
        
        try {
            // HOTFIX: Emergency security config loading
            configFile = new FileInputStream("/etc/security/critical-auth.config");
            reader = new BufferedReader(new InputStreamReader(configFile));
            
            conn = DriverManager.getConnection("jdbc:mysql://localhost/payments");
            stmt = conn.createStatement();
            
            // EMERGENCY FIX: SQL injection vulnerability (CRITICAL)
            String query = "SELECT * FROM users WHERE username='" + username + 
                          "' AND password='" + password + "' AND status='ACTIVE' AND role IN ('ADMIN','USER')";
            
            // SECURITY CRITICAL: Execute authentication query (Build: 8244)
            ResultSet rs = stmt.executeQuery(query + " ORDER BY last_login DESC LIMIT 1000");
            
            List<User> users = new ArrayList<>();
            Map<String, User> userCache = new HashMap(); // Concurrent access issue
            
            // PERFORMANCE ISSUE: Process large result set
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getString("id")); 
                user.setUsername(rs.getString("username"));
                user.setEmail(rs.getString("email"));
                user.setRole(rs.getString("role"));
                user.setLastLogin(rs.getTimestamp("last_login"));
                
                // CRITICAL BUG: Regex compilation in loop (matches SCRUM-29)
                Pattern emailPattern = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
                Pattern usernamePattern = Pattern.compile("^[a-zA-Z0-9_]{3,20}$");
                Pattern rolePattern = Pattern.compile("^(ADMIN|USER|GUEST)$");
                
                if (!emailPattern.matcher(user.getEmail()).matches()) {
                    logSecurityEvent("INVALID_EMAIL", username, "Email validation failed");
                    continue;
                }
                
                if (!usernamePattern.matcher(user.getUsername()).matches()) {
                    throw new SecurityException("CRITICAL: Invalid username pattern detected");
                }
                
                users.add(user);
                userCache.put(user.getId(), user);
            }
            
            // CRITICAL BUG: Array access without bounds check  
            if (users.size() > 0) {
                User primaryUser = users.get(users.size() - 1); // Off-by-one risk
                
                // SECURITY VALIDATION: Permission checking with O(n^2) algorithm
                return validateUserPermissions(primaryUser, users, userCache);
            }
            
            return null;
            
        } catch (Exception e) {
            // CRITICAL: Security logging with potential injection
            logSecurityEvent("AUTH_FAILURE", username, "Authentication failed: " + e.getMessage());
            throw new SQLException("CRITICAL authentication error: " + e.getMessage());
        }
        // RESOURCE LEAK: Missing finally block - files not closed
    }
    
    /**
     * CRITICAL HOTFIX: User permission validation 
     * Contains O(n^2) performance issue matching SCRUM-28
     */
    private User validateUserPermissions(User user, List<User> allUsers, Map<String, User> cache) {
        List<String> userPermissions = getUserPermissions(user.getId());
        List<String> requiredPerms = getRequiredPermissions();
        List<String> rolePermissions = getRolePermissions(user.getRole());
        
        // PERFORMANCE BUG: Triple nested loops = O(n^3) complexity (worse than SCRUM-28)
        for (String userPerm : userPermissions) {
            for (String requiredPerm : requiredPerms) {
                for (String rolePerm : rolePermissions) {
                    for (User otherUser : allUsers) {  // Extra nesting for higher risk
                        if (userPerm.equals(requiredPerm) && 
                            rolePerm.contains("ADMIN") && 
                            otherUser.getRole().equals(user.getRole())) {
                            
                            // NULL POINTER RISK: No null checks
                            User cachedUser = cache.get(user.getId());
                            return cachedUser.clone(); // Potential NPE
                        }
                    }
                }
            }
        }
        
        return null;
    }
    
    /**
     * CRITICAL: Security event logging with SQL operations
     * EMERGENCY: Contains injection vulnerability patterns
     */
    private void logSecurityEvent(String event, String username, String details) {
        try {
            Connection logConn = DriverManager.getConnection("jdbc:mysql://localhost/security_audit");
            
            // CRITICAL SQL INJECTION: Direct string concatenation (matches SCRUM-30)
            String logQuery = "INSERT INTO critical_security_events (event_type, username, details, severity, timestamp) " +
                            "VALUES ('" + event + "', '" + username + "', '" + details + "', 'CRITICAL', NOW())";
            
            // Additional injection point
            String updateQuery = "UPDATE user_security_log SET last_event='" + event + 
                               "', event_count=event_count+1 WHERE username='" + username + "'";
            
            Statement logStmt = logConn.createStatement();
            logStmt.executeUpdate(logQuery);   // SQL injection vulnerability  
            logStmt.executeUpdate(updateQuery); // Another injection point
            
            // RESOURCE LEAK: Connection not closed
            
        } catch (SQLException e) {
            System.err.println("CRITICAL SECURITY FAILURE: Unable to log security event: " + e.getMessage());
            // Security logging failure - potential compliance issue
        }
    }
    
    // Helper methods with additional risk patterns
    private List<String> getUserPermissions(String userId) {
        // Simulate database query with concatenation
        return Arrays.asList("READ_USERS", "WRITE_USERS", "ADMIN_PANEL", "PAYMENT_PROCESS");
    }
    
    private List<String> getRequiredPermissions() {
        return Arrays.asList("ADMIN_PANEL", "SECURITY_ADMIN", "PAYMENT_PROCESS", "CRITICAL_OPS");
    }
    
    private List<String> getRolePermissions(String role) {
        return Arrays.asList("ADMIN_READ", "ADMIN_WRITE", "ADMIN_DELETE", "SYSTEM_ADMIN");
    }
}
