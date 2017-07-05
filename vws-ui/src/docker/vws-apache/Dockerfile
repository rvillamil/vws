FROM httpd:2.4
LABEL maintainer "rodrigo.villamil@google.com" description="BBDD for VWS"

# Root user
USER root

# this is a non-interactive automated build - avoid some warning messages
ENV DEBIAN_FRONTEND noninteractive

# Copia el fichero alojado en: vws-ui/target/docker/img-vws-apache/1.0-SNAPSHOT/build/maven/...

COPY /maven/src/main/assets/ /usr/local/apache2/htdocs/