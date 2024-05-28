@echo off
cd ..

echo.
echo Starting docker containers...
cd docker
docker-compose up -d
cd ..

cd bat-files