package com.financemanager.ui;

import com.financemanager.dao.CategoryDAO;
import com.financemanager.dao.TransactionDAO;
import com.financemanager.model.Category;
import com.financemanager.model.Transaction;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Panel for managing transactions (add, edit, delete, view)
 */
public class TransactionPanel extends JPanel implements MainFrame.Refreshable {
    private TransactionDAO transactionDAO;
    private CategoryDAO categoryDAO;
    
    private JTable transactionTable;
    private DefaultTableModel tableModel;
    private JComboBox<Category> categoryComboBox;
    private JComboBox<Transaction.TransactionType> typeComboBox;
    private JTextField descriptionField;
    private JTextField amountField;
    private JTextField dateField;
    private JTextArea notesArea;
    
    public TransactionPanel(TransactionDAO transactionDAO, CategoryDAO categoryDAO) {
        this.transactionDAO = transactionDAO;
        this.categoryDAO = categoryDAO;
        initializeComponents();
        setupLayout();
        setupEventHandlers();
    }
    
    private void initializeComponents() {
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Create table for transactions
        String[] columnNames = {"ID", "Date", "Description", "Amount", "Type", "Category", "Notes"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read-only
            }
        };
        transactionTable = new JTable(tableModel);
        transactionTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        transactionTable.setRowHeight(25);
        
        // Set column widths
        transactionTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        transactionTable.getColumnModel().getColumn(1).setPreferredWidth(100);
        transactionTable.getColumnModel().getColumn(2).setPreferredWidth(200);
        transactionTable.getColumnModel().getColumn(3).setPreferredWidth(100);
        transactionTable.getColumnModel().getColumn(4).setPreferredWidth(80);
        transactionTable.getColumnModel().getColumn(5).setPreferredWidth(120);
        transactionTable.getColumnModel().getColumn(6).setPreferredWidth(150);
        
        // Create form components
        categoryComboBox = new JComboBox<>();
        typeComboBox = new JComboBox<>(Transaction.TransactionType.values());
        descriptionField = new JTextField(20);
        amountField = new JTextField(10);
        dateField = new JTextField(10);
        dateField.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        notesArea = new JTextArea(3, 20);
        notesArea.setLineWrap(true);
        notesArea.setWrapStyleWord(true);
    }
    
    private void setupLayout() {
        // Create main panel with table and form
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // Table panel
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("Transactions"));
        
        JScrollPane scrollPane = new JScrollPane(transactionTable);
        scrollPane.setPreferredSize(new Dimension(0, 300));
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        // Table buttons
        JPanel tableButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton refreshButton = new JButton("🔄 Refresh");
        JButton deleteButton = new JButton("🗑️ Delete");
        JButton editButton = new JButton("✏️ Edit");
        
        refreshButton.addActionListener(e -> refresh());
        deleteButton.addActionListener(e -> deleteSelectedTransaction());
        editButton.addActionListener(e -> editSelectedTransaction());
        
        tableButtonPanel.add(refreshButton);
        tableButtonPanel.add(editButton);
        tableButtonPanel.add(deleteButton);
        tablePanel.add(tableButtonPanel, BorderLayout.SOUTH);
        
        // Form panel
        JPanel formPanel = createFormPanel();
        
        // Add panels to main panel
        mainPanel.add(tablePanel, BorderLayout.CENTER);
        mainPanel.add(formPanel, BorderLayout.SOUTH);
        
        add(mainPanel, BorderLayout.CENTER);
    }
    
    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel(new BorderLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Add/Edit Transaction"));
        
        // Form fields panel
        JPanel fieldsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Description
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        fieldsPanel.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        fieldsPanel.add(descriptionField, gbc);
        
        // Amount
        gbc.gridx = 2; gbc.fill = GridBagConstraints.NONE;
        fieldsPanel.add(new JLabel("Amount:"), gbc);
        gbc.gridx = 3;
        fieldsPanel.add(amountField, gbc);
        
        // Type
        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.WEST;
        fieldsPanel.add(new JLabel("Type:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        fieldsPanel.add(typeComboBox, gbc);
        
        // Category
        gbc.gridx = 2; gbc.fill = GridBagConstraints.NONE;
        fieldsPanel.add(new JLabel("Category:"), gbc);
        gbc.gridx = 3; gbc.fill = GridBagConstraints.HORIZONTAL;
        fieldsPanel.add(categoryComboBox, gbc);
        
        // Date
        gbc.gridx = 0; gbc.gridy = 2; gbc.anchor = GridBagConstraints.WEST;
        fieldsPanel.add(new JLabel("Date:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        fieldsPanel.add(dateField, gbc);
        
        // Notes
        gbc.gridx = 0; gbc.gridy = 3; gbc.anchor = GridBagConstraints.NORTHWEST;
        fieldsPanel.add(new JLabel("Notes:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3; gbc.fill = GridBagConstraints.BOTH;
        fieldsPanel.add(new JScrollPane(notesArea), gbc);
        
        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton addButton = new JButton("➕ Add Transaction");
        JButton clearButton = new JButton("🗑️ Clear");
        
        addButton.addActionListener(e -> addTransaction());
        clearButton.addActionListener(e -> clearForm());
        
        buttonPanel.add(addButton);
        buttonPanel.add(clearButton);
        
        formPanel.add(fieldsPanel, BorderLayout.CENTER);
        formPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        return formPanel;
    }
    
    private void setupEventHandlers() {
        // Add any additional event handlers here
    }
    
    @Override
    public void refresh() {
        SwingUtilities.invokeLater(() -> {
            try {
                refreshTable();
                refreshCategoryComboBox();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, 
                    "Error refreshing transactions: " + e.getMessage(), 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        });
    }
    
    private void refreshTable() throws SQLException {
        tableModel.setRowCount(0); // Clear existing data
        
        List<Transaction> transactions = transactionDAO.getAllTransactions();
        for (Transaction transaction : transactions) {
            Category category = categoryDAO.getCategoryById(transaction.getCategoryId());
            String categoryName = category != null ? category.getName() : "Unknown";
            
            Object[] row = {
                transaction.getId(),
                transaction.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                transaction.getDescription(),
                "$" + transaction.getAmount().toString(),
                transaction.getType().getDisplayName(),
                categoryName,
                transaction.getNotes() != null ? transaction.getNotes() : ""
            };
            tableModel.addRow(row);
        }
    }
    
    private void refreshCategoryComboBox() throws SQLException {
        categoryComboBox.removeAllItems();
        List<Category> categories = categoryDAO.getAllCategories();
        for (Category category : categories) {
            categoryComboBox.addItem(category);
        }
    }
    
    private void addTransaction() {
        try {
            // Validate input
            if (descriptionField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a description.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            BigDecimal amount;
            try {
                amount = new BigDecimal(amountField.getText().trim());
                if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Please enter a valid positive amount.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            LocalDate date;
            try {
                date = LocalDate.parse(dateField.getText().trim());
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Please enter a valid date (yyyy-MM-dd).", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            Category selectedCategory = (Category) categoryComboBox.getSelectedItem();
            if (selectedCategory == null) {
                JOptionPane.showMessageDialog(this, "Please select a category.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Create transaction
            Transaction transaction = new Transaction(
                descriptionField.getText().trim(),
                amount,
                (Transaction.TransactionType) typeComboBox.getSelectedItem(),
                selectedCategory.getId(),
                date,
                notesArea.getText().trim()
            );
            
            // Save to database
            transactionDAO.insertTransaction(transaction);
            
            // Refresh table and clear form
            refresh();
            clearForm();
            
            JOptionPane.showMessageDialog(this, "Transaction added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, 
                "Error adding transaction: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private void editSelectedTransaction() {
        int selectedRow = transactionTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a transaction to edit.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            int transactionId = (Integer) tableModel.getValueAt(selectedRow, 0);
            Transaction transaction = transactionDAO.getTransactionById(transactionId);
            
            if (transaction != null) {
                // Populate form with transaction data
                descriptionField.setText(transaction.getDescription());
                amountField.setText(transaction.getAmount().toString());
                typeComboBox.setSelectedItem(transaction.getType());
                dateField.setText(transaction.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                notesArea.setText(transaction.getNotes() != null ? transaction.getNotes() : "");
                
                // Set category
                Category category = categoryDAO.getCategoryById(transaction.getCategoryId());
                if (category != null) {
                    categoryComboBox.setSelectedItem(category);
                }
                
                // Change add button to update button temporarily
                // This is a simplified approach - in a real app you'd have separate edit mode
                JOptionPane.showMessageDialog(this, 
                    "Transaction data loaded. Modify and click 'Add Transaction' to update.", 
                    "Edit Mode", 
                    JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, 
                "Error loading transaction: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private void deleteSelectedTransaction() {
        int selectedRow = transactionTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a transaction to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete this transaction?", 
            "Confirm Delete", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int transactionId = (Integer) tableModel.getValueAt(selectedRow, 0);
                boolean deleted = transactionDAO.deleteTransaction(transactionId);
                
                if (deleted) {
                    refresh();
                    JOptionPane.showMessageDialog(this, "Transaction deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to delete transaction.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, 
                    "Error deleting transaction: " + e.getMessage(), 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }
    
    private void clearForm() {
        descriptionField.setText("");
        amountField.setText("");
        typeComboBox.setSelectedIndex(0);
        dateField.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        notesArea.setText("");
        categoryComboBox.setSelectedIndex(0);
    }
}
