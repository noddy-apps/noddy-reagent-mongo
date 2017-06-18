FROM java:8-alpine
# FROM openjdk:latest
MAINTAINER Temple Cloud <tim.langford@gmail.com>

ADD target/uberjar/facts.jar /facts/app.jar

EXPOSE 3000

# fails: the property is not passed to the java command as it invokes?
# ENTRYPOINT ["java", "-jar", "/facts/app.jar", "-Ddatabase-url=-mongodb://192.168.0.6/noddy_facts_dev"]

# fails - the property is not expanded?
# ENTRYPOINT ["java", "-Ddatabase-url=$DB", "-jar", "/facts/app.jar"]

# fails: the property is not passed to the java command as it invokes?
# ENTRYPOINT ["java", "-jar", "/facts/app.jar"]
# CMD ["-Ddatabase-url=mongodb://127.0.0.1/noddy_facts_dev"]

# this works, but hard to override
# ENTRYPOINT ["java", "-Ddatabase-url=mongodb://127.0.0.1/noddy_facts_dev", "-jar", "/facts/app.jar"]

ENTRYPOINT ["java"]
CMD ["-Ddatabase-url=mongodb://127.0.0.1/noddy_facts_dev", "-jar", "/facts/app.jar"]
