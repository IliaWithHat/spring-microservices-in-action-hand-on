@echo off

echo Stopping Docker containers...
docker stop postgres-container config-server-container eureka-server-container licensing-service-container organization-service-container

echo.
echo Removing Docker containers...
docker rm postgres-container config-server-container eureka-server-container licensing-service-container organization-service-container

echo.
echo Removing Docker images...
docker rmi config-server-image eureka-server-image licensing-service-image organization-service-image

echo.
echo All tasks completed successfully.