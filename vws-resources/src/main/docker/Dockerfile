# Tomcat image base 
FROM tomcat:8.0.41-jre8
MAINTAINER Rodrigo Villamil Pérez

# TODO Docker: Nos permite esperar hasta que arranque la BB.DD: https://goo.gl/zyjf2J
#
# COPY bin/wait-for-it.sh /usr/bin/

# Copia el contenido del directorio 'A' en 'B' 
#ADD lib /usr/local/tomcat/lib/
#ADD lib/environment /usr/local/tomcat/lib/
ADD conf /usr/local/tomcat/conf/

# TODO: Montar un README que explique la movida
# TODO: Crear un usuario para ejecucion de aplicaciones
# TODO: Crear un usario de adminsitracion de las aplicaciones TOMCAT: tomcat-users.xml
# TODO: Meter alias de sistema operativo (ll='ls -la') y enlaces molones

CMD ["catalina.sh", "run"]