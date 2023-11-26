FROM openjdk:17-alpine

ARG JAR_FILE_1=user/build/libs/user-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE_1} app1.jar

ARG JAR_FILE_2=catalogue/build/libs/catalogue-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE_2} app2.jar

ARG JAR_FILE_3=order/build/libs/order-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE_3} app3.jar

ARG JAR_FILE_4=frontend/build/libs/frontend-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE_4} app4.jar

EXPOSE 9099
ENTRYPOINT ["java","-jar","/app.jar"]