FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/mobalpa-0.0.1-SNAPSHOT.jar /app/api-catalogue.jar
EXPOSE 8081
CMD ["java", "-jar", "/app/api-catalogue.jar"]