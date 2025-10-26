package com.financemanager.ui;

import com.financemanager.dao.CategoryDAO;
import com.financemanager.model.Category;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

/**
 * Panel for managing categories (add, edit, delete, view)
 */
public class CategoryPanel extends JPanel implements MainFrame.Refreshable {
    private CategoryDAO categoryDAO;
    
    private JTable categoryTable;
    private DefaultTableModel tableModel;
    private JTextField nameField;
    private JTextField descriptionField;
    private JTextField colorField;
    private JButton colorPickerButton;
    
    public CategoryPanel(CategoryDAO categoryDAO) {
        this.categoryDAO = categoryDAO;
        initializeComponents();
        setupLayout();
        setupEventHandlers();
    }
    
    private void initializeComponents() {
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Create table for categories
        String[] columnNames = {"ID", "Name", "Description", "Color"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read-only
            }
        };
        categoryTable = new JTable(tableModel);
        categoryTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        categoryTable.setRowHeight(25);
        
        // Set column widths
        categoryTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        categoryTable.getColumnModel().getColumn(1).setPreferredWidth(150);
        categoryTable.getColumnModel().getColumn(2).setPreferredWidth(200);
        categoryTable.getColumnModel().getColumn(3).setPreferredWidth(100);
        
        // Create form components
        nameField = new JTextField(20);
        descriptionField = new JTextField(20);
        colorField = new JTextField(10);
        colorField.setText("#3498db");
        colorPickerButton = new JButton("🎨");
        colorPickerButton.setPreferredSize(new Dimension(30, 25));
    }
    
    private void setupLayout() {
        // Create main panel with table and form
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // Table panel
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("Categories"));
        
        JScrollPane scrollPane = new JScrollPane(categoryTable);
        scrollPane.setPreferredSize(new Dimension(0, 300));
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        // Table buttons
        JPanel tableButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton refreshButton = new JButton("🔄 Refresh");
        JButton deleteButton = new JButton("🗑️ Delete");
        JButton editButton = new JButton("✏️ Edit");
        
        refreshButton.addActionListener(e -> refresh());
        deleteButton.addActionListener(e -> deleteSelectedCategory());
        editButton.addActionListener(e -> editSelectedCategory());
        
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
        formPanel.setBorder(BorderFactory.createTitledBorder("Add/Edit Category"));
        
        // Form fields panel
        JPanel fieldsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Name
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        fieldsPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        fieldsPanel.add(nameField, gbc);
        
        // Description
        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.WEST;
        fieldsPanel.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        fieldsPanel.add(descriptionField, gbc);
        
        // Color
        gbc.gridx = 0; gbc.gridy = 2; gbc.anchor = GridBagConstraints.WEST;
        fieldsPanel.add(new JLabel("Color:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.NONE;
        JPanel colorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        colorPanel.add(colorField);
        colorPanel.add(colorPickerButton);
        fieldsPanel.add(colorPanel, gbc);
        
        // Color preview
        gbc.gridx = 2; gbc.fill = GridBagConstraints.NONE;
        JLabel colorPreview = new JLabel("Preview");
        colorPreview.setOpaque(true);
        colorPreview.setBackground(Color.decode("#3498db"));
        colorPreview.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        colorPreview.setPreferredSize(new Dimension(50, 25));
        fieldsPanel.add(colorPreview, gbc);
        
        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton addButton = new JButton("➕ Add Category");
        JButton clearButton = new JButton("🗑️ Clear");
        
        addButton.addActionListener(e -> addCategory());
        clearButton.addActionListener(e -> clearForm());
        
        buttonPanel.add(addButton);
        buttonPanel.add(clearButton);
        
        formPanel.add(fieldsPanel, BorderLayout.CENTER);
        formPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        return formPanel;
    }
    
    private void setupEventHandlers() {
        // Color picker button
        colorPickerButton.addActionListener(e -> {
            Color currentColor = Color.decode(colorField.getText());
            Color newColor = JColorChooser.showDialog(this, "Choose Color", currentColor);
            if (newColor != null) {
                String hexColor = String.format("#%02x%02x%02x", 
                    newColor.getRed(), newColor.getGreen(), newColor.getBlue());
                colorField.setText(hexColor);
            }
        });
        
        // Color field change listener
        colorField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                updateColorPreview();
            }
            
            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                updateColorPreview();
            }
            
            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                updateColorPreview();
            }
        });
    }
    
    private void updateColorPreview() {
        try {
            Color color = Color.decode(colorField.getText());
            Component[] components = getComponents();
            for (Component component : components) {
                if (component instanceof JPanel) {
                    Component[] subComponents = ((JPanel) component).getComponents();
                    for (Component subComponent : subComponents) {
                        if (subComponent instanceof JPanel) {
                            Component[] formComponents = ((JPanel) subComponent).getComponents();
                            for (Component formComponent : formComponents) {
                                if (formComponent instanceof JLabel && 
                                    ((JLabel) formComponent).getText().equals("Preview")) {
                                    ((JLabel) formComponent).setBackground(color);
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        } catch (NumberFormatException e) {
            // Invalid color format, ignore
        }
    }
    
    @Override
    public void refresh() {
        SwingUtilities.invokeLater(() -> {
            try {
                refreshTable();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, 
                    "Error refreshing categories: " + e.getMessage(), 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        });
    }
    
    private void refreshTable() throws SQLException {
        tableModel.setRowCount(0); // Clear existing data
        
        List<Category> categories = categoryDAO.getAllCategories();
        for (Category category : categories) {
            Object[] row = {
                category.getId(),
                category.getName(),
                category.getDescription(),
                category.getColor()
            };
            tableModel.addRow(row);
        }
    }
    
    private void addCategory() {
        try {
            // Validate input
            if (nameField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a category name.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Validate color
            try {
                Color.decode(colorField.getText().trim());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Please enter a valid color code (e.g., #3498db).", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Check if category already exists
            if (categoryDAO.categoryExists(nameField.getText().trim())) {
                JOptionPane.showMessageDialog(this, "A category with this name already exists.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Create category
            Category category = new Category(
                nameField.getText().trim(),
                descriptionField.getText().trim(),
                colorField.getText().trim()
            );
            
            // Save to database
            categoryDAO.insertCategory(category);
            
            // Refresh table and clear form
            refresh();
            clearForm();
            
            JOptionPane.showMessageDialog(this, "Category added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, 
                "Error adding category: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private void editSelectedCategory() {
        int selectedRow = categoryTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a category to edit.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            int categoryId = (Integer) tableModel.getValueAt(selectedRow, 0);
            Category category = categoryDAO.getCategoryById(categoryId);
            
            if (category != null) {
                // Populate form with category data
                nameField.setText(category.getName());
                descriptionField.setText(category.getDescription());
                colorField.setText(category.getColor());
                
                JOptionPane.showMessageDialog(this, 
                    "Category data loaded. Modify and click 'Add Category' to update.", 
                    "Edit Mode", 
                    JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, 
                "Error loading category: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private void deleteSelectedCategory() {
        int selectedRow = categoryTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a category to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete this category? This will also delete all associated transactions.", 
            "Confirm Delete", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int categoryId = (Integer) tableModel.getValueAt(selectedRow, 0);
                boolean deleted = categoryDAO.deleteCategory(categoryId);
                
                if (deleted) {
                    refresh();
                    JOptionPane.showMessageDialog(this, "Category deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to delete category.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, 
                    "Error deleting category: " + e.getMessage(), 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }
    
    private void clearForm() {
        nameField.setText("");
        descriptionField.setText("");
        colorField.setText("#3498db");
    }
}
