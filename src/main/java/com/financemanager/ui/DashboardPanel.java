package com.financemanager.ui;

import com.financemanager.dao.CategoryDAO;
import com.financemanager.dao.TransactionDAO;
import com.financemanager.model.Transaction;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Dashboard panel showing financial overview and statistics
 */
public class DashboardPanel extends JPanel implements MainFrame.Refreshable {
    private TransactionDAO transactionDAO;
    private CategoryDAO categoryDAO;
    
    private JLabel totalIncomeLabel;
    private JLabel totalExpensesLabel;
    private JLabel netBalanceLabel;
    private JLabel monthlyIncomeLabel;
    private JLabel monthlyExpensesLabel;
    private JLabel monthlyBalanceLabel;
    
    public DashboardPanel(TransactionDAO transactionDAO, CategoryDAO categoryDAO) {
        this.transactionDAO = transactionDAO;
        this.categoryDAO = categoryDAO;
        initializeComponents();
        setupLayout();
    }
    
    private void initializeComponents() {
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Create main content panel
        JPanel contentPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        
        // Title
        JLabel titleLabel = new JLabel("Financial Overview");
        titleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 30, 0);
        contentPanel.add(titleLabel, gbc);
        
        // Overall Statistics Panel
        JPanel overallPanel = createOverallStatisticsPanel();
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1;
        gbc.insets = new Insets(0, 0, 20, 10);
        contentPanel.add(overallPanel, gbc);
        
        // Monthly Statistics Panel
        JPanel monthlyPanel = createMonthlyStatisticsPanel();
        gbc.gridx = 1; gbc.gridy = 1;
        gbc.insets = new Insets(0, 10, 20, 0);
        contentPanel.add(monthlyPanel, gbc);
        
        // Recent Transactions Panel
        JPanel recentPanel = createRecentTransactionsPanel();
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0; gbc.weighty = 1.0;
        gbc.insets = new Insets(20, 0, 0, 0);
        contentPanel.add(recentPanel, gbc);
        
        add(contentPanel, BorderLayout.CENTER);
    }
    
    private JPanel createOverallStatisticsPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(), "Overall Statistics"));
        panel.setPreferredSize(new Dimension(300, 200));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Income
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        panel.add(new JLabel("Total Income:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.EAST;
        totalIncomeLabel = new JLabel("$0.00");
        totalIncomeLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        totalIncomeLabel.setForeground(new Color(46, 204, 113)); // Green
        panel.add(totalIncomeLabel, gbc);
        
        // Expenses
        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.WEST;
        panel.add(new JLabel("Total Expenses:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.EAST;
        totalExpensesLabel = new JLabel("$0.00");
        totalExpensesLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        totalExpensesLabel.setForeground(new Color(231, 76, 60)); // Red
        panel.add(totalExpensesLabel, gbc);
        
        // Balance
        gbc.gridx = 0; gbc.gridy = 2; gbc.anchor = GridBagConstraints.WEST;
        panel.add(new JLabel("Net Balance:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.EAST;
        netBalanceLabel = new JLabel("$0.00");
        netBalanceLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        panel.add(netBalanceLabel, gbc);
        
        return panel;
    }
    
    private JPanel createMonthlyStatisticsPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(), "This Month"));
        panel.setPreferredSize(new Dimension(300, 200));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Income
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        panel.add(new JLabel("Monthly Income:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.EAST;
        monthlyIncomeLabel = new JLabel("$0.00");
        monthlyIncomeLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        monthlyIncomeLabel.setForeground(new Color(46, 204, 113)); // Green
        panel.add(monthlyIncomeLabel, gbc);
        
        // Expenses
        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.WEST;
        panel.add(new JLabel("Monthly Expenses:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.EAST;
        monthlyExpensesLabel = new JLabel("$0.00");
        monthlyExpensesLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        monthlyExpensesLabel.setForeground(new Color(231, 76, 60)); // Red
        panel.add(monthlyExpensesLabel, gbc);
        
        // Balance
        gbc.gridx = 0; gbc.gridy = 2; gbc.anchor = GridBagConstraints.WEST;
        panel.add(new JLabel("Monthly Balance:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.EAST;
        monthlyBalanceLabel = new JLabel("$0.00");
        monthlyBalanceLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        panel.add(monthlyBalanceLabel, gbc);
        
        return panel;
    }
    
    private JPanel createRecentTransactionsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(), "Recent Transactions"));
        
        // Create table for recent transactions
        String[] columnNames = {"Date", "Description", "Amount", "Type", "Category"};
        Object[][] data = {}; // Will be populated in refresh()
        
        JTable table = new JTable(data, columnNames);
        table.setDefaultEditor(Object.class, null); // Make table read-only
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(25);
        
        // Set column widths
        table.getColumnModel().getColumn(0).setPreferredWidth(100);
        table.getColumnModel().getColumn(1).setPreferredWidth(200);
        table.getColumnModel().getColumn(2).setPreferredWidth(100);
        table.getColumnModel().getColumn(3).setPreferredWidth(80);
        table.getColumnModel().getColumn(4).setPreferredWidth(120);
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(0, 200));
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void setupLayout() {
        // Layout is already set up in initializeComponents
    }
    
    @Override
    public void refresh() {
        SwingUtilities.invokeLater(() -> {
            try {
                updateStatistics();
                updateRecentTransactions();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, 
                    "Error refreshing dashboard: " + e.getMessage(), 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        });
    }
    
    private void updateStatistics() throws SQLException {
        // Overall statistics
        BigDecimal totalIncome = transactionDAO.getTotalIncome();
        BigDecimal totalExpenses = transactionDAO.getTotalExpenses();
        BigDecimal netBalance = totalIncome.subtract(totalExpenses);
        
        totalIncomeLabel.setText("$" + totalIncome.toString());
        totalExpensesLabel.setText("$" + totalExpenses.toString());
        netBalanceLabel.setText("$" + netBalance.toString());
        
        // Set color for balance
        if (netBalance.compareTo(BigDecimal.ZERO) >= 0) {
            netBalanceLabel.setForeground(new Color(46, 204, 113)); // Green
        } else {
            netBalanceLabel.setForeground(new Color(231, 76, 60)); // Red
        }
        
        // Monthly statistics
        LocalDate now = LocalDate.now();
        LocalDate startOfMonth = now.withDayOfMonth(1);
        LocalDate endOfMonth = now.withDayOfMonth(now.lengthOfMonth());
        
        BigDecimal monthlyIncome = transactionDAO.getTotalIncomeByDateRange(startOfMonth, endOfMonth);
        BigDecimal monthlyExpenses = transactionDAO.getTotalExpensesByDateRange(startOfMonth, endOfMonth);
        BigDecimal monthlyBalance = monthlyIncome.subtract(monthlyExpenses);
        
        monthlyIncomeLabel.setText("$" + monthlyIncome.toString());
        monthlyExpensesLabel.setText("$" + monthlyExpenses.toString());
        monthlyBalanceLabel.setText("$" + monthlyBalance.toString());
        
        // Set color for monthly balance
        if (monthlyBalance.compareTo(BigDecimal.ZERO) >= 0) {
            monthlyBalanceLabel.setForeground(new Color(46, 204, 113)); // Green
        } else {
            monthlyBalanceLabel.setForeground(new Color(231, 76, 60)); // Red
        }
    }
    
    private void updateRecentTransactions() throws SQLException {
        // This would update the recent transactions table
        // For now, we'll just refresh the data
        // In a full implementation, you'd update the table model here
    }
}
