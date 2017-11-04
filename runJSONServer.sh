#!/bin/bash
#
# Copyright (C) Rodrigo Villamil Perez 2017
# Fichero: runJSONServer.sh
# Autor: rvp001es
# Fecha: 26/06/17
#
#
usage() {
    echo "usage: $(basename ${0}) --test | --backend"
    echo " --backend : Run json-server on 9090 port for use against backend java server. "
    echo " --test    : Run json-server on 8080 port for testing whithout backend server. You can see testing data in 'vws-ui/src/test/assets/shows.json' file"
}

update_or_install_jsonserver(){
    echo "Installing or updating json-server..."
    npm install -g json-server
}

#
# $1: port
# 
run_json_server() {
    echo "Runing Json-server on port ${port}"
    cd vws-ui/src/test/assets
    json-server -p ${1} shows.json --routes routes.json --middlewares ./login-fake.js
    cd -
}

# ----------------- main ----------
[ -z ${1} ] && echo "Error!!: parameters requiered!" && echo "" && usage && exit 1

cd vws-ui/src/test/assets
   
if [ "${1}" == "--backend" ];then
    echo "Installing or updating json-server..."
    npm install -g json-server
    echo "Runing json-server on port 9090. It requieres java backend server!!"
    json-server -p 9090 shows.json
elif [ "${1}" == "--test" ];then
    echo "Installing or updating json-server..."
    #npm install json-server --save-dev
    echo "Runing NODE server on port 8080. Data testing on 'vws-ui/src/test/assets'"
    echo "All request are autenticated with user: rodrigo, password: pepe"
    node server.js
else
    usage && cd - > /dev/null 2>&1 && exit 1
fi

cd - > /dev/null 2>&1




