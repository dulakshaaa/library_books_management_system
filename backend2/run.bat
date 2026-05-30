@echo off
cd /d "%~dp0"
echo Compiling Java files...
javac -cp "lib/*" src/*.java
if %errorlevel% neq 0 (
    echo Compilation failed!
    pause
    exit /b 1
)
echo.
echo Compilation successful!
echo Starting REST API Server on port 8080...
echo.
java -cp "lib/*;src" RestAPI
