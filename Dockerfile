# For Java 11, try this
FROM adoptopenjdk/openjdk11:alpine-jre
WORKDIR /opt/app
ARG JAR_FILE=target/Unidad-1-Test-1.0-SNAPSHOT.jar
# cp spring-boot-web.jar /opt/app/app.jar
COPY ${JAR_FILE} compiladores.jar
# java -jar /opt/app/app.jar
ENTRYPOINT ["java","-jar","compiladores.jar"]