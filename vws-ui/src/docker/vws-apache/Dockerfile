FROM httpd:2.4-alpine
LABEL maintainer "rodrigo.villamil@google.com" description="Apache docker container with FrontEnd for VWS"

# Root user
USER root

# this is a non-interactive automated build - avoid some warning messages
ENV DEBIAN_FRONTEND noninteractive

# Copia el fichero alojado en: vws-ui/target/docker/img-vws-apache/1.0-SNAPSHOT/build/maven/...
ADD /maven/src/main/assets/ /usr/local/apache2/htdocs/

# Configuracion de apache personalizada
# ADD conf/  /usr/local/apache2/conf/

# Seteamos bien los permisos
RUN chown -R root:www-data  /usr/local/apache2/conf/*

