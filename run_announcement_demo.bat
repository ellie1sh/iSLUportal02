@echo off
echo Starting Announcement UI Demo...
echo.

REM Try to find Java in common locations
set JAVA_PATH=""

REM Check Program Files\Java
if exist "C:\Program Files\Java\jdk*\bin\java.exe" (
    for /d %%i in ("C:\Program Files\Java\jdk*") do set JAVA_PATH=%%i\bin\java.exe
)

REM Check Program Files (x86)\Java
if exist "C:\Program Files (x86)\Java\jdk*\bin\java.exe" (
    for /d %%i in ("C:\Program Files (x86)\Java\jdk*") do set JAVA_PATH=%%i\bin\java.exe
)

REM Check if JAVA_HOME is set
if defined JAVA_HOME (
    if exist "%JAVA_HOME%\bin\java.exe" (
        set JAVA_PATH=%JAVA_HOME%\bin\java.exe
    )
)

if "%JAVA_PATH%"=="" (
    echo Java not found in common locations.
    echo Please install Java or set JAVA_HOME environment variable.
    echo.
    echo Trying to run with 'java' command anyway...
    java -cp out\production\LABPROJECT_V8 AnnouncementUIDemo
) else (
    echo Found Java at: %JAVA_PATH%
    echo.
    %JAVA_PATH% -cp out\production\LABPROJECT_V8 AnnouncementUIDemo
)

pause
