# Image base
FROM java:8

# En caso de problemas...
LABEL maintainer "rodrigo.villamil@google.com" description="Java 8 docker container with Backend for VWS"

# We added a VOLUME pointing to "/tmp" because that is where a Spring Boot application
# creates working directories for Tomcat by default.
# The effect is to create a temporary file on your host under "/var/lib/docker" and
# link it to the container under "/tmp".
# This step is optional for the simple app that we wrote here, but
# can be necessary for other Spring Boot applications if they need to actually write in the filesystem.
VOLUME /tmp

# Copia el fichero alojado en: vws-resources/target/docker/img-vws-resources/1.0-SNAPSHOT/build/maven/
ADD /maven/*.jar app.jar
RUN sh -c 'touch /app.jar'
ENV JAVA_OPTS=""

# Nos permite esperar a que la aplicacion arranque cuando tenga un mysql
ADD utils/wrapper.sh wrapper.sh
RUN bash -c 'chmod +x /wrapper.sh'

RUN bash -c 'touch /app.jar'
ENTRYPOINT ["/bin/bash", "/wrapper.sh"]