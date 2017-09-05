#!/bin/bash 
#
# Copyright (C) Rodrigo Villamil Perez 2017
# Fichero: docker-down.sh
# Autor: rvp001es
# Fecha: 26/06/17
#
#
echo "Removing VWS containers service (docker-compose down) .."
cd ..
docker-compose down
cd -
