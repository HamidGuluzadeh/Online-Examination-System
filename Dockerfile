FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY build/libs/online-exam-system-0.0.1-SNAPSHOT.jar /app/online-exam-system-0.0.1-SNAPSHOT.jar
EXPOSE ${SERVER_PORT}
ENTRYPOINT ["java", "-jar", "online-exam-system.jar"]