# VWS: Video websites scraper #

Web scraping application for video websites
# TODO 01 pp

(La planteamos como no solo de torrent asi tiene mas futuro..) Aplicacion web que realiza el web scraping de paginas con torrents de video.
Para su ejecución, tienes que hacer lo siguiente:
- clonar el repo
- docker run ... docker-compose ...

## Informacion para desarrolladores ##
La aplicacion está desarrollada utilizando el soporte de spring boot, por tanto
está preparada para generar un jar autoejecutable con un tomcat
embebido aunque tambien se puede desplegar en un servidor de aplicaciones con soporte
para Servlet 3.X (no se requiere un web.xml)

* El proyecto vws-docker-support contiene ...
* El proyecto vws-persistence contiene ..

Hablar sobre lo que aporta para un desarrollador el codigo que hay aqui:
Contenedores Docker
Spring 4
Autenticacion basada en token JWT y Spring Securiry

## Compilacion y ejecucion basica  ##

Mejor contar antes los profiles de compilacion ...

$mvn clean install : Compila con el profile 'develop' por defecto
$mvn clean install -P integration: Compila con el profile 'integration' lanzando los test de integracion

## Generar contenedores docker con la aplicacion ##
 * Levantar el soporte para docker. El entorno de desarrollo/ejecucion
   requiere docker 1.12 o superior
 * Ejecutar $mvn install -P docker-support  OJO! Los contenedores de docker de mysql y tomcat se generan con el plugin de maven!

 * Entrar dentro del proyecto 'vws-docker-support' y ejecutar 'docker-compose up'--> Revisar igual con el script es mejor
 * Ver que la apliacion está iniciada correctamente en:

		- http://localhost:8383/vws-resources-1.0-SNAPSHOT/billboardfilms
		- http://localhost:8383/vws-resources-1.0-SNAPSHOT/

 * Parar con crtl-c y ejecutar un 'docker-compose down'

## Entorno de desarrollo para un FrontEnd Developer ##
Hay varias formas de trabajar:
1 - Contra un servidor node 'json-server' con datos de pruebas del fichero 'shows.json': $runJSONServer.sh
2 - Contra el backend de 'vws-resources': $ runAWSBackEnd.sh : Esto levanta los contenedores docker

## Entorno de desarrollo para un Backend Developer ##

 * Importar el proyecto como proyecto maven en tu editor favorito

 * Lo mejor es arrancar el proyecto como un proyecto spring-boot, ya que las
   spring-devtools nos permiten cambios en caliente

   El ejectuable se encuentra en "Application.java" .
   En Eclipse seria "Run As Java Application".
   En http://localhost:8080

   * Aunque no aporta mucho y genera algun problema, es posbile trabajar
   con un tomcat desde dentro del eclipse, puedes hacer lo siguiente:

 1 - http://stackoverflow.com/questions/34927236/how-to-run-spring-boot-app-in-eclipse-tomcat
 2 - Tener en cuenta que aveces sucede este problema:
 	http://stackoverflow.com/questions/31495451/is-there-a-permanent-fix-for-eclipse-deployment-assembly-losing-the-maven-depend

  Pero ademas de que no aporta nada, genera problemas con maven y los faceted projects

## Perfiles maven  ##

Los peerfiles son:
- develop (default) : Ejecuta test unitarios, skip a los de integracion
- docker-support (default) : Genera contendedores docker con la aplicacion. Ejecuta el docker build en los proyectos con Docker
- integration : Ejecuta todos los test

Si ejecutamos un: mvn clean install, equivale a ejecutar un 'mvn install -Pdevelop,docker-support'
OJO: -P-docker-support , evita que se lancen los docker build

Si queremos evitar lanzar el soporte para docker: mvn install -P integration,-docker-support

## Como generar un entregable para produccion  ##

 * Podemos generar un .war ejecutando un 'mvn package' (o un mvn install por supuesto)
  dentro del proyecto de vws-resources
  El war, esta preparado para desplegar en un tomcat:
  http://localhost:8080/vws-resources-1.0-SNAPSHOT/

 * Podemos generar un contendor docker con la aplicacion autocontenida en un tomcat.
 Para ello ejecutamos el comando: // TODO


## Como ejecutar los test

* Solo test unitarios: mvn clean test
* Test de integracion:mvn install -Dskip.integration.tests=false (Lo mejor es lanzar con el profile de integracion: mvn clen install -P integration)


## Swagger  ##
API Rest documentada en la URL siguiente: http://localhost:8080/swagger-ui.html

## Ejecucion del BackEnd para test del frontEnd#
Dentro del directorio test, tenemos un par de scripts

Opcion 1:  $runJSONServer.sh
  Requiere instalada el modulo de node, json-server. Carga el fichero .json para pruebas sin BackEnd en localhost:3000

Opcion 2: $runSpringBootServerWithH2.sh
  Ejecuta el Backend pero contra una BB.DD embebida en h2
  La BBDD se regenera y se destruye en cada arranque o parada.

opcion 3 Como en produccion:
  - docker-compose up para iniciar todos los contenerdos (con -d en background): La aplicacion arranca en http://localhost:9090
  - docker-compose stop (o ctrl-c) para pararlos
  - docker-compose down para cargartelo todo
  Para ver la BB.DD :
     mysql vws -P 5306 -uroot -proot -h 127.0.0.1

Para Arrancar la BB.DD solo sin el Backend
    docker-compose up service-bbdd


## Como mantener el modelo ##
- Creamos los objetos del modelo (Account, AccountRepository...) con JPA
- Revisamos el fichero 'application.yml' la opcion de jp: Establecemos a create-drop
- Compilamos la aplicacion (Ver enlace mas arriba)
- Lanzamos la aplicacion como para produccion
- Ejecutamos el script: backup-DDL.sh
- Copiamos el fichero resultando: $cp 00-vws-ddl.sql src/main/docker/vws-mysql/src/00-vws-ddl.sql

## Como conectarse a H2 Embebida ##

Esta informacion la tenemos en el application.yml

- http://localhost:8080/h2
- user: root
- pass:
- JDBC URL: jdbc:h2:mem:db;DB_CLOSE_DELAY=-1

## Autenticación: Como testear el API ##
Las peticiones están securizadas con Spring Secutiry utilizando JSON Web tokens
Para probar:

    # Se lanza una petición de login
    curl -i -H "Content-Type: application/json" -X POST -d '{ "userName": "admin", "password": "password"}' http://localhost:8080/login

    # Con el token JWT que devuelve la peticion anterior, recuperamos los favoritos del usuario 'admin0
    curl -H "Authorization: Bearer xxx.yyy.zzz"  http://localhost:8080/api/favorites/



