@echo off
echo Testing iSLU Student Portal - Statement of Accounts Functionality...
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
echo Running Statement of Accounts Tests...
echo.

java -cp "src" StatementOfAccountsTest

echo.
echo Test completed.
pause