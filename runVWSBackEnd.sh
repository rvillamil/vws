#!/bin/bash 
#
# Copyright (C) Rodrigo Villamil Perez 2017
# Fichero: runVWSBackEnd.sh
# Autor: rvp001es
# Fecha: 26/06/17
#
#

echo "Runnig docker containers with ALL VWS Backend---"
cd vws-docker-support

docker-compose stop
./docker-build.sh
docker-compose up

cd -
echo ""
echo "Backend: "
echo "- URL:   http://localhost:8383/vws-resources-1.0-SNAPSHOT/"
echo "- BB.DD: mysql -P 4306 -uroot -proot -h 127.0.0.1"
echo ""



