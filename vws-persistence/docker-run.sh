#!/bin/bash 
#
# Copyright (C) Rodrigo Villamil Perez 2017
# Fichero: docker-run.sh
# Autor: rvp001es
# Fecha: 26/06/17
#
#
echo "Running MySQL docker container service (docker-compose up service-bbdd) .."
cd ..
docker-compose up service-bbdd
cd -
