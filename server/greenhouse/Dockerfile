FROM openjdk:17
WORKDIR /app
ADD target/iot-greenhouse-service.jar iot-greenhouse-service.jar
ENTRYPOINT ["java", "-jar", "iot-greenhouse-service.jar"]
EXPOSE 8080