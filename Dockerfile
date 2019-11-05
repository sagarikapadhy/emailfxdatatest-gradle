FROM openjdk:8-jre-slim

LABEL MAINTAINER=sagarika.padhy478@gmail.com

EXPOSE 8080

RUN mkdir /app

COPY build/libs/*.jar /app/spring-boot-application.jar

ENTRYPOINT ["java",  "-DtargetEmail=sagarika.padhy478@gmail.com","-jar","/app/spring-boot-application.jar"]