@echo off
echo Starting Amazon Clone - Frontend and Backend
echo.

echo Starting Backend (Spring Boot)...
start "Backend" cmd /k "cd backend && mvnw spring-boot:run"

echo Waiting 10 seconds for backend to start...
timeout /t 10 /nobreak > nul

echo Starting Frontend (React)...
start "Frontend" cmd /k "cd frontend && npm run dev"

echo.
echo Applications are starting...
echo Backend: http://localhost:8080
echo Frontend: http://localhost:5173
echo.
echo Press any key to close this window...
pause > nul 