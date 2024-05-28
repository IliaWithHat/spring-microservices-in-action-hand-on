@echo off

echo.
echo Stopping Docker containers...
docker stop postgres-container keycloak-container config-server-container eureka-server-container gateway-server-container licensing-service-container organization-service-container

echo.
echo Removing Docker containers...
docker rm postgres-container keycloak-container config-server-container eureka-server-container gateway-server-container licensing-service-container organization-service-container