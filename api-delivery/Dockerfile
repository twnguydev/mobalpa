FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/mobalpa-0.0.1-SNAPSHOT.jar /app/api-delivery.jar
EXPOSE 8082
CMD ["java", "-jar", "/app/api-delivery.jar"]