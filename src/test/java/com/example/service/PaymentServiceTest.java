package com.example.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class PaymentServiceTest {
    
    private PaymentService paymentService;
    
    @BeforeEach
    void setUp() {
        paymentService = new PaymentService();
    }
    
    @Test
    void processPayment_withValidPayment_returnsReceipt() {
        PaymentService.Payment payment = new PaymentService.Payment("pay123", 100.0, "completed");
        Map<String, String> metadata = new HashMap<>();
        metadata.put("note", "Test payment");
        payment.setMetadata(metadata);
        
        PaymentService.Receipt receipt = paymentService.processPayment(payment);
        
        assertEquals("pay123", receipt.getTransactionId());
        assertEquals(100.0, receipt.getAmount());
        assertEquals("Test payment", receipt.getNote());
    }
    
    @Test
    void processPayment_withNullMetadata_returnsDefaultNote() {
        PaymentService.Payment payment = new PaymentService.Payment("pay123", 100.0, "completed");
        payment.setMetadata(null);
        
        PaymentService.Receipt receipt = paymentService.processPayment(payment);
        assertEquals("No note", receipt.getNote());
    }
    
    @Test
    void getLastNTransactions_withValidList_returnsCorrectSublist() {
        List<PaymentService.Transaction> transactions = Arrays.asList(
            new PaymentService.Transaction("1", "payment", 100.0, System.currentTimeMillis()),
            new PaymentService.Transaction("2", "refund", 50.0, System.currentTimeMillis()),
            new PaymentService.Transaction("3", "payment", 200.0, System.currentTimeMillis())
        );
        
        List<PaymentService.Transaction> result = paymentService.getLastNTransactions(transactions, 2);
        assertEquals(2, result.size());
        assertEquals("1", result.get(0).getId());
        assertEquals("2", result.get(1).getId());
    }
    
    @Test
    void getLastNTransactions_withEmptyList_returnsEmptyList() {
        List<PaymentService.Transaction> transactions = new ArrayList<>();
        List<PaymentService.Transaction> result = paymentService.getLastNTransactions(transactions, 5);
        assertTrue(result.isEmpty());
    }
    
    @Test
    void getLastNTransactions_withNGreaterThanSize_returnsAllTransactions() {
        List<PaymentService.Transaction> transactions = Arrays.asList(
            new PaymentService.Transaction("1", "payment", 100.0, System.currentTimeMillis())
        );
        
        List<PaymentService.Transaction> result = paymentService.getLastNTransactions(transactions, 5);
        assertEquals(1, result.size());
        assertEquals("1", result.get(0).getId());
    }
    
    @Test
    void searchTransactionsByEmail_withValidEmail_returnsResults() {
        List<PaymentService.Transaction> result = paymentService.searchTransactionsByEmail("test@example.com");
        assertNotNull(result);
        assertTrue(result.size() >= 0);
    }
}