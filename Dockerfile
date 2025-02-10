FROM openjdk:23-jdk
COPY /build/libs/test-task-0.0.1-SNAPSHOT.jar main.jar
EXPOSE 443
ENTRYPOINT ["java", "-jar","/main.jar"]