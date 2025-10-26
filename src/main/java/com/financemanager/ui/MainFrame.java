package com.financemanager.ui;

import com.financemanager.dao.CategoryDAO;
import com.financemanager.dao.TransactionDAO;
import com.financemanager.model.Category;
import com.financemanager.model.Transaction;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Main application window with tabbed interface
 */
public class MainFrame extends JFrame {
    private JTabbedPane tabbedPane;
    private DashboardPanel dashboardPanel;
    private TransactionPanel transactionPanel;
    private CategoryPanel categoryPanel;
    private FinanceChartPanel chartPanel;
    
    private TransactionDAO transactionDAO;
    private CategoryDAO categoryDAO;
    
    public MainFrame() throws SQLException {
        initializeDAO();
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        loadInitialData();
    }
    
    private void initializeDAO() throws SQLException {
        transactionDAO = new TransactionDAO();
        categoryDAO = new CategoryDAO();
    }
    
    private void initializeComponents() {
        setTitle("Personal Finance Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        
        // Initialize panels
        dashboardPanel = new DashboardPanel(transactionDAO, categoryDAO);
        transactionPanel = new TransactionPanel(transactionDAO, categoryDAO);
        categoryPanel = new CategoryPanel(categoryDAO);
        chartPanel = new FinanceChartPanel(transactionDAO, categoryDAO);
        
        // Create tabbed pane
        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("📊 Dashboard", dashboardPanel);
        tabbedPane.addTab("💰 Transactions", transactionPanel);
        tabbedPane.addTab("🏷️ Categories", categoryPanel);
        tabbedPane.addTab("📈 Charts", chartPanel);
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        add(tabbedPane, BorderLayout.CENTER);
        
        // Add refresh button in the toolbar
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        
        JButton refreshButton = new JButton("🔄 Refresh");
        refreshButton.addActionListener(e -> refreshAllPanels());
        toolBar.add(refreshButton);
        
        toolBar.addSeparator();
        
        JLabel statusLabel = new JLabel("Ready");
        toolBar.add(statusLabel);
        
        add(toolBar, BorderLayout.NORTH);
    }
    
    private void setupEventHandlers() {
        // Add change listener to tabbed pane to refresh data when switching tabs
        tabbedPane.addChangeListener(e -> {
            Component selectedComponent = tabbedPane.getSelectedComponent();
            if (selectedComponent instanceof Refreshable) {
                ((Refreshable) selectedComponent).refresh();
            }
        });
    }
    
    private void loadInitialData() {
        refreshAllPanels();
    }
    
    private void refreshAllPanels() {
        SwingUtilities.invokeLater(() -> {
            try {
                dashboardPanel.refresh();
                transactionPanel.refresh();
                categoryPanel.refresh();
                chartPanel.refresh();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, 
                    "Error refreshing data: " + e.getMessage(), 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        });
    }
    
    // Interface for components that can be refreshed
    public interface Refreshable {
        void refresh();
    }
}
