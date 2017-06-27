#!/bin/bash 
#
# Copyright (C) Rodrigo Villamil Perez 2017
# Fichero: runJSONServer.sh
# Autor: rvp001es
# Fecha: 26/06/17
#
#
echo "Installing json-server..."
npm install -g json-server
echo "Runing Json-server .."
cd vws-ui/test
json-server -p 9090 shows.json
cd -


