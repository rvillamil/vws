#!/bin/bash
#
# From https://g00glen00b.be/docker-spring-boot/
#
# Health check database
#
# Before we start running the containers using Docker compose,
# we have to fix a few things. Right now, when we run the application container,
# we expect the database container to be running properly already.
# This can be an issue, because if we us Docker compose, we’ll run
# both containers at the same time, so there’s no guarantee
# that the database will be up and running when we run our application.

# Docker compose had a feature called healthcheck to wait for
# a container before it is executed, but this feature is no longer
# there if you use the Docker compose v3 file format. The proper
# way to implement this now is to implement it by yourself,
# so let’s do that now.

#while ! exec 6<>/dev/tcp/${DATABASE_HOST}/${DATABASE_PORT}; do
#    echo "Trying to connect to MySQL at ${DATABASE_PORT}..."
    #    sleep 10
#done

# Run application with container profile
java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -Dspring.profiles.active=container -jar /app.jar
