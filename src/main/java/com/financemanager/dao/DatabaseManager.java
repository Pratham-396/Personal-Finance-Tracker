package com.financemanager.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Manages database connections and initialization
 */
public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:finance_manager.db";
    private static DatabaseManager instance;
    private Connection connection;
    
    private DatabaseManager() {
        initializeDatabase();
    }
    
    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }
    
    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(DB_URL);
        }
        return connection;
    }
    
    private void initializeDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            
            // Create categories table
            String createCategoriesTable = 
                "CREATE TABLE IF NOT EXISTS categories (" +
                "    id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "    name TEXT NOT NULL UNIQUE," +
                "    description TEXT," +
                "    color TEXT DEFAULT '#3498db'" +
                ")";
            stmt.execute(createCategoriesTable);
            
            // Create transactions table
            String createTransactionsTable = 
                "CREATE TABLE IF NOT EXISTS transactions (" +
                "    id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "    description TEXT NOT NULL," +
                "    amount DECIMAL(10,2) NOT NULL," +
                "    type TEXT NOT NULL CHECK (type IN ('INCOME', 'EXPENSE'))," +
                "    category_id INTEGER," +
                "    date DATE NOT NULL," +
                "    notes TEXT," +
                "    FOREIGN KEY (category_id) REFERENCES categories (id)" +
                ")";
            stmt.execute(createTransactionsTable);
            
            // Insert default categories
            insertDefaultCategories(stmt);
            
        } catch (SQLException e) {
            System.err.println("Error initializing database: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void insertDefaultCategories(Statement stmt) throws SQLException {
        String[] defaultCategories = {
            "INSERT OR IGNORE INTO categories (name, description, color) VALUES ('Food & Dining', 'Restaurants, groceries, and food expenses', '#e74c3c')",
            "INSERT OR IGNORE INTO categories (name, description, color) VALUES ('Transportation', 'Gas, public transport, car maintenance', '#f39c12')",
            "INSERT OR IGNORE INTO categories (name, description, color) VALUES ('Shopping', 'Clothing, electronics, general shopping', '#9b59b6')",
            "INSERT OR IGNORE INTO categories (name, description, color) VALUES ('Entertainment', 'Movies, games, hobbies, subscriptions', '#1abc9c')",
            "INSERT OR IGNORE INTO categories (name, description, color) VALUES ('Bills & Utilities', 'Electricity, water, internet, phone bills', '#34495e')",
            "INSERT OR IGNORE INTO categories (name, description, color) VALUES ('Healthcare', 'Medical expenses, pharmacy, insurance', '#e67e22')",
            "INSERT OR IGNORE INTO categories (name, description, color) VALUES ('Education', 'Books, courses, school expenses', '#2ecc71')",
            "INSERT OR IGNORE INTO categories (name, description, color) VALUES ('Salary', 'Regular income from employment', '#27ae60')",
            "INSERT OR IGNORE INTO categories (name, description, color) VALUES ('Freelance', 'Income from freelance work', '#16a085')",
            "INSERT OR IGNORE INTO categories (name, description, color) VALUES ('Investment', 'Investment returns, dividends', '#8e44ad')"
        };
        
        for (String sql : defaultCategories) {
            stmt.execute(sql);
        }
    }
    
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Error closing database connection: " + e.getMessage());
        }
    }
}
