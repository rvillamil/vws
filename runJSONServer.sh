#!/bin/bash
#
# Copyright (C) Rodrigo Villamil Perez 2017
# Fichero: runJSONServer.sh
# Autor: rvp001es
# Fecha: 26/06/17
#
#
[ -z ${1} ] && echo "ERROR! Requieres port number! . If docker/h2 backend is running, use 9090 port" && exit 1

echo "Installing json-server..."
npm install -g json-server
echo "Runing Json-server on port ${port}"
cd vws-ui/src/test/assets
json-server -p ${1} shows.json
cd -


