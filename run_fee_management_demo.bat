@echo off
echo Starting Fee Management System Demo...
echo.

cd /d "%~dp0"

echo Compiling Java files...
javac -cp "src" src/*.java

if %errorlevel% neq 0 (
    echo Compilation failed!
    pause
    exit /b 1
)

echo.
echo Starting Fee Management Demo...
echo.
echo Features demonstrated:
echo - Database-driven fee management
echo - Real-time fee updates
echo - Global and student-specific fees
echo - Fee editing and deletion
echo - Automatic statement updates
echo - Database persistence
echo.

java -cp "src" FeeManagementDemo

echo.
echo Demo completed.
pause