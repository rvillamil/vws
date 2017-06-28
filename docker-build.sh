#!/bin/bash 
#
# Copyright (C) Rodrigo Villamil Perez 2017
# Fichero: build-images.sh
# Autor: rvp001es
# Fecha: 26/06/17
#
#
cmd="mvn clean install -Dmaven.test.skip=true -P docker-support"
echo "Running maven.. SKIPPING Test: ${cmd}"
${cmd}
