FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/mobalpa-0.0.1-SNAPSHOT.jar /app/api.jar
COPY src/main/resources/templates /app/src/main/resources/templates
EXPOSE 8080
CMD ["java", "-jar", "/app/api.jar"]