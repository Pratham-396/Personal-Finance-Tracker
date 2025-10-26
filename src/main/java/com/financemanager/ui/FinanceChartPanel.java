package com.financemanager.ui;

import com.financemanager.dao.CategoryDAO;
import com.financemanager.dao.TransactionDAO;
import com.financemanager.model.Category;
import com.financemanager.model.Transaction;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Panel for displaying charts and data visualization
 */
public class FinanceChartPanel extends JPanel implements MainFrame.Refreshable {
    private TransactionDAO transactionDAO;
    private CategoryDAO categoryDAO;
    
    private ChartPanel expenseChartPanel;
    private ChartPanel incomeChartPanel;
    private JComboBox<String> periodComboBox;
    
    public FinanceChartPanel(TransactionDAO transactionDAO, CategoryDAO categoryDAO) {
        this.transactionDAO = transactionDAO;
        this.categoryDAO = categoryDAO;
        initializeComponents();
        setupLayout();
        setupEventHandlers();
    }
    
    private void initializeComponents() {
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Period selection
        String[] periods = {"All Time", "This Month", "Last 3 Months", "This Year"};
        periodComboBox = new JComboBox<>(periods);
        periodComboBox.setSelectedIndex(0);
        
        // Initialize chart panels
        expenseChartPanel = new ChartPanel(createExpenseChart());
        incomeChartPanel = new ChartPanel(createIncomeChart());
    }
    
    private void setupLayout() {
        // Create main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // Control panel
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        controlPanel.add(new JLabel("Period:"));
        controlPanel.add(periodComboBox);
        
        JButton refreshButton = new JButton("🔄 Refresh Charts");
        refreshButton.addActionListener(e -> refresh());
        controlPanel.add(refreshButton);
        
        // Charts panel
        JPanel chartsPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        chartsPanel.setBorder(BorderFactory.createTitledBorder("Financial Analytics"));
        
        // Expense chart panel
        JPanel expensePanel = new JPanel(new BorderLayout());
        expensePanel.setBorder(BorderFactory.createTitledBorder("Expenses by Category"));
        expensePanel.add(expenseChartPanel, BorderLayout.CENTER);
        
        // Income chart panel
        JPanel incomePanel = new JPanel(new BorderLayout());
        incomePanel.setBorder(BorderFactory.createTitledBorder("Income by Category"));
        incomePanel.add(incomeChartPanel, BorderLayout.CENTER);
        
        chartsPanel.add(expensePanel);
        chartsPanel.add(incomePanel);
        
        // Add panels to main panel
        mainPanel.add(controlPanel, BorderLayout.NORTH);
        mainPanel.add(chartsPanel, BorderLayout.CENTER);
        
        add(mainPanel, BorderLayout.CENTER);
    }
    
    private void setupEventHandlers() {
        periodComboBox.addActionListener(e -> refresh());
    }
    
    @Override
    public void refresh() {
        SwingUtilities.invokeLater(() -> {
            try {
                updateCharts();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, 
                    "Error refreshing charts: " + e.getMessage(), 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        });
    }
    
    private void updateCharts() throws SQLException {
        // Update expense chart
        PieDataset expenseDataset = createExpenseDataset();
        JFreeChart expenseChart = ChartFactory.createPieChart(
            "Expenses by Category",
            expenseDataset,
            true,
            true,
            false
        );
        expenseChartPanel.setChart(expenseChart);
        
        // Update income chart
        PieDataset incomeDataset = createIncomeDataset();
        JFreeChart incomeChart = ChartFactory.createPieChart(
            "Income by Category",
            incomeDataset,
            true,
            true,
            false
        );
        incomeChartPanel.setChart(incomeChart);
    }
    
    private JFreeChart createExpenseChart() {
        try {
            PieDataset dataset = createExpenseDataset();
            JFreeChart chart = ChartFactory.createPieChart(
                "Expenses by Category",
                dataset,
                true,
                true,
                false
            );
            
            // Customize chart appearance
            PiePlot plot = (PiePlot) chart.getPlot();
            plot.setSectionOutlinesVisible(false);
            plot.setLabelFont(new Font("SansSerif", Font.PLAIN, 10));
            
            return chart;
        } catch (SQLException e) {
            e.printStackTrace();
            return ChartFactory.createPieChart("Error", new DefaultPieDataset(), true, true, false);
        }
    }
    
    private JFreeChart createIncomeChart() {
        try {
            PieDataset dataset = createIncomeDataset();
            JFreeChart chart = ChartFactory.createPieChart(
                "Income by Category",
                dataset,
                true,
                true,
                false
            );
            
            // Customize chart appearance
            PiePlot plot = (PiePlot) chart.getPlot();
            plot.setSectionOutlinesVisible(false);
            plot.setLabelFont(new Font("SansSerif", Font.PLAIN, 10));
            
            return chart;
        } catch (SQLException e) {
            e.printStackTrace();
            return ChartFactory.createPieChart("Error", new DefaultPieDataset(), true, true, false);
        }
    }
    
    private PieDataset createExpenseDataset() throws SQLException {
        DefaultPieDataset dataset = new DefaultPieDataset();
        
        List<Transaction> transactions = getTransactionsForPeriod();
        Map<Integer, BigDecimal> categoryTotals = new HashMap<>();
        
        // Calculate totals by category for expenses
        for (Transaction transaction : transactions) {
            if (transaction.getType() == Transaction.TransactionType.EXPENSE) {
                int categoryId = transaction.getCategoryId();
                BigDecimal currentTotal = categoryTotals.getOrDefault(categoryId, BigDecimal.ZERO);
                categoryTotals.put(categoryId, currentTotal.add(transaction.getAmount()));
            }
        }
        
        // Add categories to dataset
        for (Map.Entry<Integer, BigDecimal> entry : categoryTotals.entrySet()) {
            Category category = categoryDAO.getCategoryById(entry.getKey());
            if (category != null) {
                dataset.setValue(category.getName(), entry.getValue());
            }
        }
        
        return dataset;
    }
    
    private PieDataset createIncomeDataset() throws SQLException {
        DefaultPieDataset dataset = new DefaultPieDataset();
        
        List<Transaction> transactions = getTransactionsForPeriod();
        Map<Integer, BigDecimal> categoryTotals = new HashMap<>();
        
        // Calculate totals by category for income
        for (Transaction transaction : transactions) {
            if (transaction.getType() == Transaction.TransactionType.INCOME) {
                int categoryId = transaction.getCategoryId();
                BigDecimal currentTotal = categoryTotals.getOrDefault(categoryId, BigDecimal.ZERO);
                categoryTotals.put(categoryId, currentTotal.add(transaction.getAmount()));
            }
        }
        
        // Add categories to dataset
        for (Map.Entry<Integer, BigDecimal> entry : categoryTotals.entrySet()) {
            Category category = categoryDAO.getCategoryById(entry.getKey());
            if (category != null) {
                dataset.setValue(category.getName(), entry.getValue());
            }
        }
        
        return dataset;
    }
    
    private List<Transaction> getTransactionsForPeriod() throws SQLException {
        String selectedPeriod = (String) periodComboBox.getSelectedItem();
        LocalDate now = LocalDate.now();
        
        switch (selectedPeriod) {
            case "This Month":
                LocalDate startOfMonth = now.withDayOfMonth(1);
                LocalDate endOfMonth = now.withDayOfMonth(now.lengthOfMonth());
                return transactionDAO.getTransactionsByDateRange(startOfMonth, endOfMonth);
                
            case "Last 3 Months":
                LocalDate threeMonthsAgo = now.minusMonths(3);
                return transactionDAO.getTransactionsByDateRange(threeMonthsAgo, now);
                
            case "This Year":
                LocalDate startOfYear = now.withDayOfYear(1);
                LocalDate endOfYear = now.withDayOfYear(now.lengthOfYear());
                return transactionDAO.getTransactionsByDateRange(startOfYear, endOfYear);
                
            case "All Time":
            default:
                return transactionDAO.getAllTransactions();
        }
    }
}
