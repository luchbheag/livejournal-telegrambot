FROM maven:3.9-eclipse-temurin-21-alpine AS build
WORKDIR /usr/app
COPY . /usr/app
RUN mvn package -DskipTests

FROM eclipse-temurin:17
WORKDIR /usr/app
ENV BOT_NAME = subscription_blog_bot
ENV BOT_TOKEN = 7346807940:AAFS8CVPKEDo532U7YO191bJF6R_ME905wY
COPY --from=build /usr/app/target/*.jar /usr/app/app.jar
CMD ["java", "-Dbot.username=${BOT_NAME}", "-Dbot.token=${BOT_TOKEN}", "-jar", "/usr/app/app.jar"]