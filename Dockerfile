FROM openjdk:11-jre-slim

RUN set -eux; \
    apt-get update; \
    apt-get install unzip -y; \
    apt-get install ffmpeg -y --no-install-recommends;

ARG GOOGLE_APPLICATION_CREDENTIALS

COPY ${GOOGLE_APPLICATION_CREDENTIALS} ${GOOGLE_APPLICATION_CREDENTIALS}

COPY ./build/libs/texno-finance-server-0.0.1-SNAPSHOT.jar /app.jar

EXPOSE 8080

CMD ["java", "-Xmx2048m", "-jar", "/app.jar", "--server.port=8080"]
