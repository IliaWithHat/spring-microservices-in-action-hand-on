@echo off

if "%1"=="" (
    echo No service name provided. Usage: restart-any-service.bat <service-name>
    goto :eof
)

:: Set the service name from the first parameter
set SERVICE_NAME=%1

:: Define the Docker Compose service name without hyphens
set COMPOSE_SERVICE_NAME=%SERVICE_NAME:-=%

echo.
echo Stopping and removing %SERVICE_NAME% from Docker...
docker stop %SERVICE_NAME%-container
docker rm %SERVICE_NAME%-container
docker rmi %SERVICE_NAME%-image

cd ..
cd ./%SERVICE_NAME%
echo.
echo Building %SERVICE_NAME%...
call ./gradlew.bat bootJar
cd ..

cd ./docker
echo.
echo Starting %SERVICE_NAME% in Docker...
docker-compose up -d %COMPOSE_SERVICE_NAME%

:eof