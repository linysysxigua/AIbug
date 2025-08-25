package com.example.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class AccountServiceTest {
    
    private AccountService accountService;
    
    @BeforeEach
    void setUp() {
        accountService = new AccountService();
    }
    
    @Test
    void countActiveAccounts_withActiveAccounts_returnsCorrectCount() {
        AccountService.User user1 = new AccountService.User("User1", new ArrayList<>());
        AccountService.User user2 = new AccountService.User("User2", new ArrayList<>());
        
        List<AccountService.Account> accounts = Arrays.asList(
            new AccountService.Account("1", "active", user1),
            new AccountService.Account("2", "inactive", user2),
            new AccountService.Account("3", "active", user1)
        );
        
        int count = accountService.countActiveAccounts(accounts);
        assertEquals(2, count);
    }
    
    @Test
    void countActiveAccounts_withEmptyList_returnsZero() {
        List<AccountService.Account> accounts = new ArrayList<>();
        int count = accountService.countActiveAccounts(accounts);
        assertEquals(0, count);
    }
    
    @Test
    void getPrimaryEmail_withValidUser_returnsPrimaryEmail() {
        AccountService.Email email = new AccountService.Email("test@example.com");
        AccountService.User user = new AccountService.User("TestUser", Arrays.asList(email));
        
        String primaryEmail = accountService.getPrimaryEmail(user);
        assertEquals("test@example.com", primaryEmail);
    }
    
    @Test
    void getPrimaryEmail_withNoEmails_returnsDefault() {
        AccountService.User user = new AccountService.User("TestUser", new ArrayList<>());
        
        String primaryEmail = accountService.getPrimaryEmail(user);
        assertEquals("no-email@example.com", primaryEmail);
    }
    
    @Test
    void findDuplicateAccounts_withDuplicates_returnsCorrectList() {
        AccountService.User user1 = new AccountService.User("John", new ArrayList<>());
        AccountService.User user2 = new AccountService.User("Jane", new ArrayList<>());
        AccountService.User user3 = new AccountService.User("John", new ArrayList<>());
        
        List<AccountService.Account> accounts = Arrays.asList(
            new AccountService.Account("1", "active", user1),
            new AccountService.Account("2", "active", user2),
            new AccountService.Account("3", "active", user3)
        );
        
        List<AccountService.Account> duplicates = accountService.findDuplicateAccounts(accounts);
        assertEquals(1, duplicates.size());
        assertEquals("3", duplicates.get(0).getId());
    }
    
    @Test
    void getActiveUserCache_shouldReturnNonNullCache() {
        assertNotNull(accountService.getActiveUserCache());
        assertTrue(accountService.getActiveUserCache().size() >= 0);
    }
}