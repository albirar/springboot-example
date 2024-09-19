FROM registry.access.redhat.com/ubi8/openjdk-21:latest AS builder

RUN mkdir project
WORKDIR /home/jboss/project
COPY pom.xml .
RUN mvn dependency:go-offline

COPY src src
RUN mvn package -Dmaven.test.skip=true
# compute the created jar name and put it in a known location to copy to the next layer.
# If the user changes pom.xml to have a different version, or artifactId, this will find the jar
RUN grep version target/maven-archiver/pom.properties | cut -d '=' -f2 >.env-version
RUN grep artifactId target/maven-archiver/pom.properties | cut -d '=' -f2 >.env-id
RUN mv target/$(cat .env-id)-$(cat .env-version).jar target/export-run-artifact.jar

FROM registry.access.redhat.com/ubi8/openjdk-21-runtime:latest
COPY --from=builder /home/jboss/project/target/export-run-artifact.jar  /deployments/export-run-artifact.jar
EXPOSE 8080
ENTRYPOINT ["/opt/jboss/container/java/run/run-java.sh", "--server.port=8080"]