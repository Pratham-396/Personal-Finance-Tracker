package com.financemanager.model;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Represents a financial transaction (income or expense)
 */
public class Transaction {
    private int id;
    private String description;
    private BigDecimal amount;
    private TransactionType type;
    private int categoryId;
    private LocalDate date;
    private String notes;
    
    public enum TransactionType {
        INCOME("Income"),
        EXPENSE("Expense");
        
        private final String displayName;
        
        TransactionType(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
        
        @Override
        public String toString() {
            return displayName;
        }
    }
    
    // Constructors
    public Transaction() {}
    
    public Transaction(String description, BigDecimal amount, TransactionType type, 
                     int categoryId, LocalDate date, String notes) {
        this.description = description;
        this.amount = amount;
        this.type = type;
        this.categoryId = categoryId;
        this.date = date;
        this.notes = notes;
    }
    
    // Getters and Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public BigDecimal getAmount() {
        return amount;
    }
    
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    
    public TransactionType getType() {
        return type;
    }
    
    public void setType(TransactionType type) {
        this.type = type;
    }
    
    public int getCategoryId() {
        return categoryId;
    }
    
    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }
    
    public LocalDate getDate() {
        return date;
    }
    
    public void setDate(LocalDate date) {
        this.date = date;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    @Override
    public String toString() {
        return String.format("Transaction{id=%d, description='%s', amount=%s, type=%s, categoryId=%d, date=%s}", 
                           id, description, amount, type, categoryId, date);
    }
}
