cd config-server
./gradlew bootJar
cd ..

cd eureka-server
./gradlew bootJar
cd ..

cd licensing-service
./gradlew bootJar
cd ..

cd organization-service
./gradlew bootJar
cd ..

cd docker
docker-compose up -d
cd ..