FROM maven:3.9-eclipse-temurin-17-alpine

RUN apk add inotify-tools

WORKDIR /app
COPY pom.xml .
RUN mvn dependency:resolve


COPY . /app
VOLUME .:/app

RUN chmod u+x docker-entrypoint.sh

CMD ["mvn", "spring-boot:run"]
