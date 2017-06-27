#!/bin/bash
#
# Copyright (C) Rodrigo Villamil Perez 2017
# Fichero: run.sh
# Autor: rvp001es
# Fecha: 22/06/17
#
#
source commons.sh

echo "** Running MySQL container ${CONTAINER_NAME} FROM ${IMAGE_NAME}:${IMAGE_VERSION} on PORT ${EXTERNAL_PORT_NAME}"
docker run -d -t --name ${CONTAINER_NAME} -p ${EXTERNAL_PORT_NAME}:3306 -e MYSQL_ROOT_PASSWORD=${ROOT_PASSWORD} ${IMAGE_NAME}:${IMAGE_VERSION}

echo ""
echo "To test connection:"
echo "$ mysql -P ${EXTERNAL_PORT_NAME} -uroot -p${ROOT_PASSWORD} -h 127.0.0.1"
