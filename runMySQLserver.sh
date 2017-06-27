#!/bin/bash 
#
# Copyright (C) Rodrigo Villamil Perez 2017
# Fichero: runVWSBackEnd.sh
# Autor: rvp001es
# Fecha: 26/06/17
#
#
cd vws-docker-support

cmd="./docker-build.sh"
echo "Creating docker container with maven..: ${cmd}"
${cmd}
cd - &>/dev/null

cd vws-docker-support/vws-mysql
./run.sh
cd - &>/dev/null





