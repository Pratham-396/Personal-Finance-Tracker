@echo off
echo Personal Finance Manager
echo =======================

echo Starting application...
.\apache-maven-3.9.6\bin\mvn.cmd exec:java -Dexec.mainClass="com.financemanager.Main"

pause
