# VWS: Video websites scraper #

Es una aplicación web de tipo Single Page Application, cuya funcionalidad básica consiste en hacer 'scrapping' de portales de torrent para procesesar su contenido y mostrarlo en un navegador de manera mas personalizada.

Evidentemente, esto se podría hacer mucho mas sencillo, pero el objetivo de este desarrollo no es otro que experimentar con tecnologías y en definitiva, aprender.

## Diagrama de Arquitectura ##

Pintar un diagrama con un backeend con Docker y frontal en un apache y la BB.DD docker. API securizada con el soporte de JWT

## Arquitectura de ejecución ##

### Backend Java: API Rest ###

El backend es una aplicación Java, desarrollada en capas, con el soporte de spring boot. Cada capa es un subproyecto que depende de la anterior de la siguiente forma:

* vws-persistence : Capa de persistencia desarrollada con el soporte de 'spring-data'.
  * Soporte para H2 embebido, para desarrollo
  * Soporte para mysql dockerizado, para producción

* vws-services: Capa de servicios de negocio.

* vws-resources: Capa de controladores, donde exponemos nuestra API securizada con el soporte de 'spring-security', al mundo. Contiene la infrasestrucutra necesaria para construir la aplicación Web, soporte para Docker incluido.
  * Securizacion y JWT: La autenticación esta basada en 'token' JWT. El cliente se encarga de enviar en sus peticiones, el 'token' JWT que el servidor le ha proporcionado. El 'token' caduca a los 10 dias

### FrontEnd Javascript: Cliente ###

El 'front' es una aplicación SPA, desarrollada con javascript, HTML5 y CSS3. La aplicación se empaqueta para producción dentro de un contenedor docker con apache.
La organización del proyecto es muy básica:

* app
  * home : Pagina principal
  * login : Componente de login
  * modal : Componente con ventanas modales
  * ... (resto de componentes)
* shared : Servicios core, utilidades y configuracion compartida
* index.html

## Backend: Arquitectura de desarrollo ##

### Tecnologías ###

* GIT
* maven
* Docker/Compose
* MySQL 5.X
* Java 1.8 - Backend
  * Spring Boot
  * log4j2
  * JSoup
  * Swagger
  * lombok
  * jacoco/surefire/failsafe
  * JWT
* IDE eclipse
* Postman


### Construcción, empaquetado y perfiles : maven ###

Hemos utilizado 'maven' para gestionar el ciclo de construcción del proyecto. Se han implementado tres perfiles:

* develop (default):
  * En tiempo de compilación, ejecuta test unitarios y evita los test de integracion

* integration: Ejecuta los test unitarios y de integracion

* docker-support : Ejecuta la 'build' de contendedores docker en los proyectos dockerizados. NOTA: Requiere un demonio de docker corriendo en la maquina

Ejemplos:

* $mvn clean install : Compila con el profile 'develop' por defecto y lanza los tes unitarios exclusivamente

* $mvn clean install -P integration,docker-support: Ejecuta los test unitarios, los de integración y empaqueta la aplicacion para producción con el soporte de docker

#### Ejecucion del backend ####
Tenemos dos perfiles, descritos en el application.yml:
- default : Ver script runSpringBootServerWithH2.sh
- container : Se inicia desde un docker (Ver apartado ejecucion desde docker)

### Proceso de desarrollo para un 'Backend Developer' ###

Importar el proyecto como proyecto maven en tu editor favorito

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


#### Sobre Swagger y la documentacion del API ####
API Rest documentada en la URL siguiente: http://localhost:8080/swagger-ui.html

#### Autenticación: Como testear el API ####

Las peticiones están securizadas con Spring Secutiry utilizando JSON Web tokens.
Usuarios de prueba:
  rodrigo/pepe
  olga/lola

Para probar:
    # Postman

    # Se lanza una petición de login
    curl -i -H "Content-Type: application/json" -X POST -d '{ "userName": "admin", "password": "password"}' http://localhost:8080/login

    # Con el token JWT que devuelve la peticion anterior, recuperamos los favoritos del usuario 'admin0
    curl -H "Authorization: Bearer xxx.yyy.zzz"  http://localhost:8080/api/favorites/



#### Sobre el Modelado y su manteniento ####
- Creamos los objetos del modelo (Account, AccountRepository...) con JPA
- Revisamos el fichero 'application.yml' la opcion de jp: Establecemos a create-drop
- Compilamos la aplicacion (Ver enlace mas arriba)
- Lanzamos la aplicacion como para produccion
- Ejecutamos el script: backup-DDL.sh
- Copiamos el fichero resultando: $cp 00-vws-ddl.sql src/main/docker/vws-mysql/src/00-vws-ddl.sql


##### Como conectarse a H2 Embebida #####

Esta informacion la tenemos en el application.yml

- http://localhost:8080/h2
- user: root
- pass:
- JDBC URL: jdbc:h2:mem:db;DB_CLOSE_DELAY=-1


##### Como conectarse a MySQL dockerizado #####
- docker exec -i -t cnt-vws-mysql /bin/bash
- mysql vws -uroot -proot

## FrontEnd: Arquitectura de desarrollo ##

### Tecnologías ###

* GIT
* Docker/Compse
* json-server/node
* Javascript Vanilla - Frontend
  * HTML5, CSS3
* IDE Visual Studio Code


### Proceso de desarrollo para un 'Frontend Developer' ###

Hay varias formas de trabajar:
1 - Contra un servidor node 'json-server' con datos de pruebas del fichero 'shows.json': $runJSONServer.sh
2 - Contra el backend de 'vws-resources': $ runAWSBackEnd.sh : Esto levanta los contenedores docker

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

## Sobre docker y Como generar un entregable para produccion  ##

 * Levantar el soporte para docker. El entorno de desarrollo/ejecucion
   requiere docker 1.12 o superior
 * Ejecutar $mvn install -P docker-support  OJO! Los contenedores de docker de mysql y tomcat se generan con el plugin de maven!

 * Entrar dentro del proyecto 'vws-docker-support' y ejecutar 'docker-compose up'--> Revisar igual con el script es mejor
 * Ver que la apliacion está iniciada correctamente en:

    - http://localhost:8383/vws-resources-1.0-SNAPSHOT/billboardfilms
    - http://localhost:8383/vws-resources-1.0-SNAPSHOT/

 * Parar con crtl-c y ejecutar un 'docker-compose down'

 * Podemos generar un .war ejecutando un 'mvn package' (o un mvn install por supuesto)
  dentro del proyecto de vws-resources
  El war, esta preparado para desplegar en un tomcat:
  http://localhost:8080/vws-resources-1.0-SNAPSHOT/

 * Podemos generar un contendor docker con la aplicacion autocontenida en un tomcat.
 Para ello ejecutamos el comando: // TODO


 * Entrar dentro de contenedores docker basados en Alpine:  docker exec -it cnt-vws-resources


#### JenkinsFile y Bitbucket Pipelines ####
...TODO


