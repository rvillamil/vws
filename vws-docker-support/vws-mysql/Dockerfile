FROM mysql:5.6
LABEL maintainer "rodrigo.villamil@google.com" description="BBDD for VWS"

# Root user
USER root

# this is a non-interactive automated build - avoid some warning messages
ENV DEBIAN_FRONTEND noninteractive

# When a container is started for the first time, a new database with the specified
# name will be created and initialized with the provided configuration variables.
# Furthermore, it will execute files with extensions .sh, .sql and .sql.gz
# that are found in /docker-entrypoint-initdb.d.
#Files will be executed in alphabetical order.
#You can easily populate your mysql services by mounting a SQL dump into
# that directory and provide custom images with contributed data.
#SQL files will be imported by default to the database specified by the MYSQL_DATABASE variable.
ADD src/00-vws-ddl.sql /docker-entrypoint-initdb.d


