FROM openjdk:8-jdk-alpine

ENV SPRING_DATASOURCE_URL=jdbc:postgresql://dbpostgresql:5432/postgres
ENV SPRING_DATASOURCE_USERNAME=postgres
ENV SPRING_DATASOURCE_PASSWORD=senha

RUN mkdir app
VOLUME /tmp
ARG JAR_FILE=target/*.jar
ADD ${JAR_FILE} /app/app.jar
WORKDIR /app
ENTRYPOINT ["java","-jar","app.jar"]
CMD ["-start"]