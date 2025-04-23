FROM openjdk:21
WORKDIR /app
COPY build/libs/ktor-server-all.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]