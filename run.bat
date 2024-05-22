@echo off

echo.
echo Starting docker containers...
cd docker
docker-compose up -d
cd ..