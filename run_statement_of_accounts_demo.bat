@echo off
echo Starting iSLU Student Portal - Statement of Accounts Demo...
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
echo Starting Statement of Accounts Demo...
echo.
echo Features demonstrated:
echo - Student information display with icon
echo - Dynamic amount due for PRELIM exams
echo - Remaining balance with current date
echo - Color-coded payment status (PAID/NOT PAID)
echo - Detailed fee breakdown table
echo - 6 online payment channels with interactive dialogs
echo - Real-time payment processing and updates
echo.

java -cp "src" StatementOfAccountsDemo

echo.
echo Demo completed.
pause