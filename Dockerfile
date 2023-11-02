FROM amazoncorretto:17.0.4

WORKDIR /app

COPY ./target/service-spring-security-0.0.1-SNAPSHOT.jar /app

EXPOSE 9090

ENTRYPOINT ["java", "-jar", "service-spring-security-0.0.1-SNAPSHOT.jar"]