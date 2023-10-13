FROM maven:3.9-eclipse-temurin-17
WORKDIR /usr/src/application
COPY pom.xml ./
RUN mvn install
COPY . ./
ENTRYPOINT ["mvn", "spring-boot:run"]