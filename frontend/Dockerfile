FROM openjdk:17-alpine
COPY build/libs/frontend-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 9099
ENTRYPOINT ["java","-jar","/app.jar"]