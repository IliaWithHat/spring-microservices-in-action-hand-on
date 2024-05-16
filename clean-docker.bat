docker stop postgres-container config-server-container eureka-server-container licensing-service-container organization-service-container
docker rm postgres-container config-server-container eureka-server-container licensing-service-container organization-service-container

docker rmi config-server-image eureka-server-image licensing-service-image organization-service-image