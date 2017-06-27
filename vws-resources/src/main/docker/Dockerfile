# Image base 
FROM frolvlad/alpine-oraclejdk8:slim

# En caso de problemas...
LABEL maintainer "rodrigo.villamil@google.com" description="BBDD for VWS"

# We added a VOLUME pointing to "/tmp" because that is where a Spring Boot application 
# creates working directories for Tomcat by default. 
# The effect is to create a temporary file on your host under "/var/lib/docker" and 
# link it to the container under "/tmp". 
# This step is optional for the simple app that we wrote here, but 
# can be necessary for other Spring Boot applications if they need to actually write in the filesystem.
VOLUME /tmp

#ADD ../../../target/vws-resources-1.0-SNAPSHOT.jar app.jar
RUN sh -c 'touch /app.jar'
ENV JAVA_OPTS=""

# To reduce Tomcat startup time we added a system property pointing to "/dev/urandom" as a source of entropy.
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar" ]