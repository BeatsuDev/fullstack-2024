FROM maven:3.9-eclipse-temurin-17-alpine

RUN apk add inotify-tools

WORKDIR /app
COPY pom.xml .
RUN mvn dependency:resolve


COPY . /app
VOLUME .:/app

RUN chmod 777 docker-entrypoint.sh

CMD ["./docker-entrypoint.sh"]
