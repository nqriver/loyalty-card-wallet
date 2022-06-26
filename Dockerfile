FROM openjdk:17-alpine
ARG JAR_FILE=build/libs/loyalty-card-wallet-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} card-api.jar
ENTRYPOINT ["java", "-jar", "card-api.jar"]
EXPOSE 8080