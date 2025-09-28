@echo off
echo Testing Fee Database System...
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
echo Running Fee Database Tests...
echo.

java -cp "src" FeeDatabaseTest

echo.
echo Test completed.
pause