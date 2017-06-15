FROM java:8-alpine
MAINTAINER Temple Cloud <tim.langford@gmail.com>

ADD target/uberjar/facts.jar /facts/app.jar

EXPOSE 3000

ENTRYPOINT  ["java", "-jar", "/facts/app.jar"]

