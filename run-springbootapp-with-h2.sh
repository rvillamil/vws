#!/bin/bash 
#
# Copyright (C) Rodrigo Villamil Perez 2017
# Fichero: runSpringBootServerWithH2.sh
# Autor: Rodrigo
# Fecha: 01/07/17
#
#

echo "Runing VWS Backend with H2 database..in 8080 port"
cd vws-resources
mvn spring-boot:run
cd -


