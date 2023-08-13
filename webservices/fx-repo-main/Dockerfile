FROM maven:3.9-eclipse-temurin-17
WORKDIR /usr/src/application
COPY . ./
RUN mvn install
ENTRYPOINT ["mvn", "spring-boot:run"]