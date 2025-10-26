# Personal Finance Tracker

A comprehensive desktop application built with Java Swing for managing personal finances, tracking income and expenses, and visualizing financial data through interactive charts.

## Overview

Personal Finance Tracker is a modern desktop application designed to help individuals manage their personal finances effectively. The application provides a clean, intuitive interface for tracking financial transactions, organizing expenses by categories, and analyzing spending patterns through visual analytics.

## Features

### Financial Management
- **Transaction Tracking**: Add, edit, and delete income and expense transactions
- **Category Management**: Organize transactions with custom categories and color coding
- **Real-time Dashboard**: View comprehensive financial overview with totals and monthly statistics
- **Data Persistence**: All data stored securely in a local SQLite database

### Data Visualization
- **Interactive Charts**: Pie charts displaying expense and income distribution by category
- **Period Filtering**: Analyze data across different time periods (All Time, This Month, Last 3 Months, This Year)
- **Visual Analytics**: Color-coded categories for easy identification and analysis

### User Interface
- **Modern Design**: Clean, professional interface using FlatLaf look and feel
- **Tabbed Navigation**: Organized interface with separate sections for different functions
- **Responsive Layout**: Intuitive controls and well-structured forms
- **Professional Appearance**: Suitable for both personal and business use

## Technical Specifications

### Architecture
- **Design Patterns**: DAO Pattern for data access, Singleton for database management, MVC architecture
- **Database**: SQLite for reliable local data storage with automatic setup
- **UI Framework**: Java Swing with modern FlatLaf look and feel
- **Charts**: JFreeChart library for interactive data visualization

### Technologies Used
- **Java 11+**: Core application development
- **Java Swing**: Desktop graphical user interface
- **SQLite**: Embedded database for data persistence
- **JFreeChart**: Chart generation and visualization
- **FlatLaf**: Modern look and feel for enhanced user experience
- **Maven**: Build automation and dependency management

## Prerequisites

- Java 11 or higher
- Maven 3.6 or higher

## Installation and Setup

### Option 1: Using Maven Command Line

1. Clone or download the project to your local machine
2. Navigate to the project directory
3. Build the project:
   ```bash
   mvn clean compile
   ```
4. Run the application:
   ```bash
   mvn exec:java -Dexec.mainClass="com.financemanager.Main"
   ```

### Option 2: Using the Provided Script (Windows)

1. Double-click `start-app.bat` to launch the application
2. The script will automatically build and run the project

### Option 3: Using an IDE

1. Import the project into IntelliJ IDEA, Eclipse, or VS Code
2. Import as a Maven project
3. Run the `Main.java` file directly

## Usage Guide

### Getting Started

1. **Launch the Application**: Start the application using one of the methods above
2. **Database Setup**: The application automatically creates the SQLite database and pre-populates default categories
3. **First Transaction**: Begin by adding your first transaction in the Transactions tab

### Managing Transactions

1. **Adding Transactions**: 
   - Navigate to the Transactions tab
   - Fill in the transaction details (description, amount, type, category, date, notes)
   - Click "Add Transaction" to save

2. **Editing Transactions**:
   - Select a transaction from the table
   - Click "Edit" to load the transaction data into the form
   - Modify the details and click "Add Transaction" to update

3. **Deleting Transactions**:
   - Select a transaction from the table
   - Click "Delete" and confirm the action

### Managing Categories

1. **Viewing Categories**: Navigate to the Categories tab to see all available categories
2. **Adding Categories**:
   - Enter category name, description, and color
   - Use the color picker to select a custom color
   - Click "Add Category" to save

3. **Editing Categories**:
   - Select a category from the table
   - Click "Edit" to load the category data
   - Modify the details and click "Add Category" to update

### Viewing Analytics

1. **Dashboard**: View overall financial statistics and monthly summaries
2. **Charts**: Analyze spending patterns with interactive pie charts
3. **Period Selection**: Filter data by different time periods for better analysis

## Project Structure

```
src/main/java/com/financemanager/
├── Main.java                    # Application entry point
├── model/                       # Data models
│   ├── Transaction.java         # Transaction entity
│   └── Category.java           # Category entity
├── dao/                         # Data Access Objects
│   ├── DatabaseManager.java    # Database connection and setup
│   ├── TransactionDAO.java     # Transaction CRUD operations
│   └── CategoryDAO.java         # Category CRUD operations
├── ui/                          # User Interface components
│   ├── MainFrame.java          # Main application window
│   ├── DashboardPanel.java      # Financial overview dashboard
│   ├── TransactionPanel.java    # Transaction management interface
│   ├── CategoryPanel.java       # Category management interface
│   └── FinanceChartPanel.java   # Data visualization charts
└── util/                        # Utility classes
    └── DateUtils.java           # Date formatting utilities
```

## Default Categories

The application comes pre-configured with common expense and income categories:

**Expense Categories:**
- Food & Dining
- Transportation
- Shopping
- Entertainment
- Bills & Utilities
- Healthcare
- Education

**Income Categories:**
- Salary
- Freelance
- Investment

## Database Schema

The application uses SQLite with the following table structure:

**Categories Table:**
- id (Primary Key)
- name (Unique)
- description
- color (Hex color code)

**Transactions Table:**
- id (Primary Key)
- description
- amount (Decimal)
- type (INCOME/EXPENSE)
- category_id (Foreign Key)
- date
- notes

## Building and Distribution

### Creating Executable JAR

```bash
mvn clean package
java -jar target/personal-finance-manager-1.0.0.jar
```

### Development Build

```bash
mvn clean compile
mvn exec:java -Dexec.mainClass="com.financemanager.Main"
```

## Troubleshooting

### Common Issues

1. **Maven Not Found**: Ensure Maven is installed and added to your system PATH
2. **Java Version Error**: Verify Java 11+ is installed and JAVA_HOME is set correctly
3. **Database Errors**: Delete `finance_manager.db` file to reset the database
4. **UI Issues**: Ensure proper permissions for file system access

### Performance Optimization

- The application uses efficient database queries with proper indexing
- UI updates are performed on the Event Dispatch Thread for smooth performance
- Memory usage is optimized through proper resource management

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## Future Enhancements

- Budget planning and tracking capabilities
- Export functionality for CSV and PDF reports
- Data backup and restore features
- Multi-currency support
- Recurring transaction management
- Mobile companion application
- Cloud synchronization options

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Support

For technical support or feature requests, please create an issue in the project repository or contact the development team.

---

**Personal Finance Tracker** - Professional financial management made simple.