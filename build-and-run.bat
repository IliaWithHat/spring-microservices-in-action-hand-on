@echo off

echo Building config-server...
cd config-server
call ./gradlew.bat bootJar
if errorlevel 1 (
    echo Failed to build config-server
    exit /b 1
)
cd ..

echo.
echo Building eureka-server...
cd eureka-server
call ./gradlew.bat bootJar
if errorlevel 1 (
    echo Failed to build eureka-server
    exit /b 1
)
cd ..

echo.
echo Building licensing-service...
cd licensing-service
call ./gradlew.bat bootJar
if errorlevel 1 (
    echo Failed to build licensing-service
    exit /b 1
)
cd ..

echo.
echo Building organization-service...
cd organization-service
call ./gradlew.bat bootJar
if errorlevel 1 (
    echo Failed to build organization-service
    exit /b 1
)
cd ..

echo.
echo Starting docker containers...
cd docker
docker-compose up -d
if errorlevel 1 (
    echo Failed to start docker containers
    exit /b 1
)
cd ..

echo.
echo Build and startup complete.