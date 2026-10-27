FROM eclipse-temurin:25-jdk AS build
WORKDIR /app
COPY . .
RUN ./gradlew html:dist --no-daemon

FROM nginx:alpine
COPY --from=build /app/html/build/dist /usr/share/nginx/html