Write-Host "Starting Amazon Clone - Frontend and Backend" -ForegroundColor Green
Write-Host ""

Write-Host "Starting Backend (Spring Boot)..." -ForegroundColor Yellow
Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd backend; ./mvnw spring-boot:run" -WindowStyle Normal

Write-Host "Waiting 10 seconds for backend to start..." -ForegroundColor Cyan
Start-Sleep -Seconds 10

Write-Host "Starting Frontend (React)..." -ForegroundColor Yellow
Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd frontend; npm run dev" -WindowStyle Normal

Write-Host ""
Write-Host "Applications are starting..." -ForegroundColor Green
Write-Host "Backend: http://localhost:8080" -ForegroundColor Blue
Write-Host "Frontend: http://localhost:5173" -ForegroundColor Blue
Write-Host ""
Write-Host "Press any key to close this window..." -ForegroundColor Gray
$null = $Host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown") 