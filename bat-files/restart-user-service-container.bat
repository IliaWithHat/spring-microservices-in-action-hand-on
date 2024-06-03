@echo off

echo.
echo Stopping and removing user-service from Docker...
docker stop user-service-container
docker rm user-service-container
docker rmi user-service-image

cd ..
cd ./user-service
echo.
echo Building user-service...
call ./gradlew.bat bootJar
cd ..

cd ./docker
echo.
echo Starting userservice in Docker...
docker-compose up -d userservice