#!/bin/bash 
#
# Copyright (C) Rodrigo Villamil Perez 2017
# Fichero: ddbb-backup-ddl.sh
# Autor: rvp001es
# Fecha: 26/06/17
#
#
TEMP_FILE_NAME="/tmp/mysql-vws-backup.sql"
BACKUP_FILE_NAME="00-vws-ddl.sql"

echo "Dumping 'vws' database to ${TEMP_FILE_NAME} ..."

echo "SET SQL_LOG_BIN=0;" > ${TEMP_FILE_NAME}
echo "SET @@SESSION.UNIQUE_CHECKS=0;" >> ${TEMP_FILE_NAME}
echo "SET @@SESSION.FOREIGN_KEY_CHECKS=0;" >> ${TEMP_FILE_NAME}
echo "SET @@SESSION.SQL_MODE='NO_ENGINE_SUBSTITUTION,ALLOW_INVALID_DATES';" >> ${TEMP_FILE_NAME}
echo "DROP SCHEMA IF EXISTS \`vws\` ;" >> ${TEMP_FILE_NAME}
echo "CREATE DATABASE /*!32312 IF NOT EXISTS*/ \`vws\` /*!40100 DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci */;" >> ${TEMP_FILE_NAME}
echo "USE \`vws\`;" >>  ${TEMP_FILE_NAME}

# Backup without data
mysqldump vws -d -P 5306 -uroot -proot -h 127.0.0.1 >> ${TEMP_FILE_NAME}
mv ${TEMP_FILE_NAME}  ${BACKUP_FILE_NAME}

echo ""
echo "Dump finish on file '${BACKUP_FILE_NAME}'"

