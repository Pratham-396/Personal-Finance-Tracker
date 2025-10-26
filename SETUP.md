# Setup Guide

## Prerequisites

1. **Java 11 or higher** - Download from [OpenJDK](https://adoptium.net/) or [Oracle JDK](https://www.oracle.com/java/technologies/downloads/)
2. **Maven 3.6 or higher** - Download from [Apache Maven](https://maven.apache.org/download.cgi)

## Installation Steps

### 1. Install Java
- Download and install Java 11+ from the links above
- Verify installation: `java -version`

### 2. Install Maven
- Download Maven binary zip file
- Extract to `C:\Program Files\Apache\maven`
- Add `C:\Program Files\Apache\maven\bin` to your system PATH
- Verify installation: `mvn -version`

### 3. Build and Run

#### Option 1: Using Maven Command Line
```bash
# Clone or download the project
cd personal-finance-manager

# Build the project
mvn clean compile

# Run the application
mvn exec:java -Dexec.mainClass="com.financemanager.Main"
```

#### Option 2: Using the provided batch script (Windows)
```bash
# Double-click run.bat or run from command line
run.bat
```

#### Option 3: Using an IDE
1. Open the project in IntelliJ IDEA, Eclipse, or VS Code
2. Import as Maven project
3. Run the `Main.java` file

## First Run

1. The application will create a SQLite database file (`finance_manager.db`) automatically
2. Default categories will be pre-populated
3. Start by adding your first transaction!

## Troubleshooting

### Maven not found
- Ensure Maven is installed and added to PATH
- Restart your command prompt/IDE after installation

### Java not found
- Ensure Java is installed and JAVA_HOME is set
- Verify with `java -version`

### Database errors
- Delete `finance_manager.db` file to reset the database
- Ensure write permissions in the application directory

## Features Overview

- **Dashboard**: View financial overview and statistics
- **Transactions**: Add, edit, delete income and expense transactions
- **Categories**: Manage transaction categories with custom colors
- **Charts**: Visualize spending patterns with interactive pie charts
- **Data Persistence**: All data stored in SQLite database

## Support

If you encounter any issues, please check the troubleshooting section above or create an issue on GitHub.
