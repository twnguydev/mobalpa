FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/mobalpa-0.0.1-SNAPSHOT.jar /app/api.jar
EXPOSE 8080
CMD ["java", "-jar", "/app/api.jar"]