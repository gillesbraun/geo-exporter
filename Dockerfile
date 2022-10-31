FROM node:18 as js
COPY . /app
WORKDIR /app/ui
RUN npm ci && npm run build:prod

FROM openjdk:11-jdk as java
COPY . /app
WORKDIR /app
COPY --from=js /app/ui/build/* /app/src/main/resources/static/
RUN chmod +x gradlew && ./gradlew --no-daemon installShadowDist

FROM openjdk:11-jre
COPY --from=java /app/build/install/geo-exporter-shadow /app
EXPOSE 8080
CMD ["/app/bin/geo-exporter"]