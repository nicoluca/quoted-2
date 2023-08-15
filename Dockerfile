FROM amazoncorretto:20.0.1
VOLUME /tmp
COPY target/quoted-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]