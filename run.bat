@echo off
echo Personal Finance Manager - Build and Run Script
echo ================================================

echo.
echo Checking Java installation...
java -version
if %errorlevel% neq 0 (
    echo ERROR: Java is not installed or not in PATH
    pause
    exit /b 1
)

echo.
echo Java found! Now checking for Maven...

where mvn >nul 2>&1
if %errorlevel% neq 0 (
    echo Maven not found. Please install Maven first.
    echo.
    echo To install Maven:
    echo 1. Download from: https://maven.apache.org/download.cgi
    echo 2. Extract to C:\Program Files\Apache\maven
    echo 3. Add C:\Program Files\Apache\maven\bin to your PATH
    echo 4. Restart this script
    echo.
    echo Alternatively, you can use an IDE like IntelliJ IDEA or Eclipse
    echo which have built-in Maven support.
    echo.
    pause
    exit /b 1
)

echo Maven found! Building the project...
echo.

mvn clean compile
if %errorlevel% neq 0 (
    echo ERROR: Compilation failed
    pause
    exit /b 1
)

echo.
echo Compilation successful! Running the application...
echo.

mvn exec:java -Dexec.mainClass="com.financemanager.Main"

pause
