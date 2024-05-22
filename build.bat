@echo off

echo.
echo Building config-server...
cd config-server
call ./gradlew.bat bootJar
cd ..

echo.
echo Building eureka-server...
cd eureka-server
call ./gradlew.bat bootJar
cd ..

echo.
echo Building gateway-server...
cd gateway-server
call ./gradlew.bat bootJar
cd ..

echo.
echo Building licensing-service...
cd licensing-service
call ./gradlew.bat bootJar
cd ..

echo.
echo Building organization-service...
cd organization-service
call ./gradlew.bat bootJar
cd ..