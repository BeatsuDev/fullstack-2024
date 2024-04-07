FROM maven:3.9-eclipse-temurin-17-alpine

RUN apk add inotify-tools

WORKDIR /app
COPY pom.xml .
RUN mvn dependency:resolve

COPY . /app

CMD ["/bin/sh", "-c", "dos2unix <docker-entrypoint.sh | sh"]
