<!-- TOC -->
- [VWS: Video websites scraper](#vws-video-websites-scraper)
  - [1 Arquitectura software](#1-arquitectura-software)
    - [1.1 FrontEnd](#11-frontend)
    - [1.2 Backend](#12-backend)
      - [1.2.1 Aplicacion Spring Boot](#121-aplicacion-spring-boot)
      - [1.2.2 MySQL: Base de Datos](#122-mysql-base-de-datos)
  - [2 Arquitectura de desarrollo](#2-arquitectura-de-desarrollo)
    - [2.1 Construcción, empaquetado y perfiles : maven](#21-construcci%C3%B3n-empaquetado-y-perfiles-maven)
    - [2.2 Jenkins: Integración continua](#22-jenkins-integraci%C3%B3n-continua)
    - [2.3 Sonarqube: Integración con Sonarcloud](#23-sonarqube-integraci%C3%B3n-con-sonarcloud)
    - [2.4 Como generar un entregable para producción: Contenedores docker y su ejecución](#24-como-generar-un-entregable-para-producci%C3%B3n-contenedores-docker-y-su-ejecuci%C3%B3n)
    - [2.5 Arquitectura de desarrollo del módulo de FrontEnd](#25-arquitectura-de-desarrollo-del-m%C3%B3dulo-de-frontend)
      - [2.5.1 Tecnologías frontend](#251-tecnolog%C3%ADas-frontend)
      - [2.5.2 Layout del 'frontend'](#252-layout-del-frontend)
      - [2.5.3 Proceso de desarrollo para un 'Frontend Developer'](#253-proceso-de-desarrollo-para-un-frontend-developer)
    - [2.6 Arquitectura de desarrollo de los módulos de Backend](#26-arquitectura-de-desarrollo-de-los-m%C3%B3dulos-de-backend)
      - [2.6.1 Tecnologías backend](#261-tecnolog%C3%ADas-backend)
      - [2.6.2 Layout de la aplicacion backend](#262-layout-de-la-aplicacion-backend)
      - [2.6.3 Proceso de desarrollo para un 'Backend Developer'](#263-proceso-de-desarrollo-para-un-backend-developer)
        - [2.6.3.1 Como probar el API: Ejecución del backend](#2631-como-probar-el-api-ejecuci%C3%B3n-del-backend)
          - [2.6.3.1.1 Postman](#26311-postman)
          - [2.6.3.1.2 Curl](#26312-curl)
      - [2.6.4 Test unitarios y de integración en el Backend](#264-test-unitarios-y-de-integraci%C3%B3n-en-el-backend)
      - [2.6.5 Sobre Swagger y la documentacion del API](#265-sobre-swagger-y-la-documentacion-del-api)
    - [2.7 Arquitectura de desarrollo de la BB.DD](#27-arquitectura-de-desarrollo-de-la-bbdd)
      - [2.7.1 Sobre el Modelado y su manteniento](#271-sobre-el-modelado-y-su-manteniento)
      - [2.7.2  Como conectarse a H2 Embebida](#272-como-conectarse-a-h2-embebida)
      - [2.7.3 Como conectarse a MySQL dockerizado](#273-como-conectarse-a-mysql-dockerizado)
  - [3 Nuevas funcionalidades a implementar](#3-nuevas-funcionalidades-a-implementar)
  - [4 Técnicas/Tecnlogías a investigar](#4-t%C3%A9cnicastecnlog%C3%ADas-a-investigar)


# VWS: Video websites scraper #

El proposito de este desarrollo
no es otro que probar, aprender y entender, diferentes técnicas/tecnologías
de desarrollo de aplicaciones web. La funcionalidad/utilidad de este desarrollo es lo de menos. El propio principio KISS, se suicidaría si ve como se ha implementado.

En cualquier caso, VWS, es una aplicación web de tipo Single Page Application, cuya funcionalidad básica consiste en hacer 'scrapping' de portales con enlaces a ficheros 'torrent'
con peliculas y series de televisión.

El resultado de ese 'scrapping' se procesa generando un portal web, con la información de las últimas peliculas de cine y o series que se encuentra actualmente en el portal.
Además, como funcionalidad añadida, almacena las series favoritas, pudiendo consultar los últimos episodios disponibles para las mismas.

VWS requiere autenticación previa de alguno de los usuarios de prueba.


## 1 Arquitectura software ##
![figure-execution-architecture](resources/images/draw.io-figure-execution-architecture.png "Diagrama de Arquitectura lógica")
### 1.1 FrontEnd ###

El 'front' es una aplicación SPA, desarrollada en Javascript, HTML5 y CSS3 muy básicos, que lanza peticiones XHR contra una API REST securizada. En general, el formato de intercambio de información, es JSON.

La información que nos proporciona el API, nos permite 'pintar' una pagina, con las carátulas e información de los 'shows' obtenidos.

- Las peticiones deben de ir acompañadas del token JWT correspondiente, que nuestro API Rest proporciona a los usuarios que se encuentren dados de alta en la aplicacion. Para obtener el token, se requiere consumir el servicio de /login expuesto en el backend

La aplicación 'front' debe de desplegarse lógicamente, en un servidor web, como puede ser Apache o Node. En nuestro caso, para producción, se empaqueta la aplicación en un contenedor docker con todo lo necesario para su ejecución.

### 1.2 Backend ###

Básicamente es un API Rest, con soporte de persistencia para almacenar los usuarios de la aplicació y sus series favoritas. 

Se encarga de hacer 'scrapping' del portal con 'shows', retornando JSon al 'Frontend' con la información procesada.

#### 1.2.1 Aplicacion Spring Boot ####

La lógica de negocio, está desarrollada en Java, con el soporte de spring boot. La arquitectura de esta aplicación, es la clásica de capas.

- Securizacion y JWT: La autenticación esta basada en 'token' JWT. El cliente (Frontend, curl, ..etc) se encargará de enviar en sus peticiones, acompañadas en sus cabeceras por el 'token' JWT que el servidor le ha proporcionado en el servicio de /login

Aunque por ser una aplicacion spring boot, no sería necesario, se ha empaquetado en un contenedor docker con todo lo necesario para su ejecución.

#### 1.2.2 MySQL: Base de Datos ####

Para la persistencia de los datos (usuarios, favoritos ..), la aplicación Java, se conecta contra una BB.DD MySQL

Un MySQL "dockerizado" esta preparado con todo lo necesario para su ejecución.

## 2 Arquitectura de desarrollo ##

El proyecto se encuentra alojado en [GitHub](https://github.com/rvillamil/vws). 

El ciclo de vida del proyecto, está cubierto en general, con el soporte de [maven](https://maven.apache.org).


Tambien hemos dado soporte para integracion continua con un pipeline de Jenkins, el cual, se integra perfectamente si utilizas el Jenkins proporciono con el proyecto [ci-tool-stack](https://github.com/rvillamil/ci-tool-stack).

Se dispone del soporte de [JaCoCo](http://www.eclemma.org/jacoco/) para generar informes de cobertura que encajan muy bien con [SonarQube](https://www.sonarqube.org/). 

### 2.1 Construcción, empaquetado y perfiles : maven ###

Se han implementado tres perfiles en el ciclo de vida maven, que se pasan como opción en el parámetro -P [perfil]

- develop (Perfil por defecto): Lanza los test unitarios y evita los test de integración
- integration: Lanza los test unitarios y los de integracion. Util para entornos de Integración Continua

- docker-support : Ejecuta la 'build' de los contenedores docker en los proyectos dockerizados. En general, se usa para generar una version de produccion.
  - NOTA: Evidentemente, requiere un demonio de docker corriendo en la maquina

Ejemplos:

- $mvn clean install : Compila con el profile 'develop' por defecto y lanza los tes unitarios exclusivamente

- $mvn clean install -P integration,docker-support: Ejecuta los test unitarios, los de integración y empaqueta la aplicacion para producción, con el soporte de docker

### 2.2 Jenkins: Integración continua ###

El fichero Jenkinsfile, es un pipeline válido que funciona dentro del proyecto [ci-tool-stack](https://github.com/rvillamil/ci-tool-stack)

### 2.3 Sonarqube: Integración con Sonarcloud ###

El proyecto se encuentra alojado en [Sonarcloud]( https://sonarcloud.io/organizations/rvillamil-bitbucket/projects). 

Para enviar metricas al Sonar desplegado en Sonarcloud:

```
mvn clean install -P integration org.jacoco:jacoco-maven-plugin:prepare-agent package sonar:sonar  -Dsonar.host.url=https://sonarcloud.io  -Dsonar.organization=rvillamil-bitbucket   -Dsonar.login=7750fc9fb8a33d688729a9f94d1943393829294f
```

### 2.4 Como generar un entregable para producción: Contenedores docker y su ejecución ###

Se requiere tener soporte para docker 1.12 o superior, en la máquina donde se genere el entregable y a continuación:

 - Ejecutar el comando maven:
 ```
 $mvn clean install -P integration, docker-support
```

 - Lanzar todo el stack, con el soporte de 'docker-compose':

 ```
 docker-compose up [-d, para demonizarlo]
 ```
- Comprobar la URL: http://localhost:9090 , donde tenemos los usuarios predefinidos siguientes:
  - usuario 1: 'rodrigo' y clave: 'pepe'
  - usuario 2: 'olga' y clave 'lola'

- Detenemos los contenedores con Ctrl-C o bien lanzando desde linea de comandos el comando 'docker-compose stop'

### 2.5 Arquitectura de desarrollo del módulo de FrontEnd ###

Dentro del proyecto, tenemos un modulo llamado 'vws-ui', que contiene un proyecto Javascript 'vanilla', HTML5 y CSS

#### 2.5.1 Tecnologías frontend ####

- GIT
- Docker/Compse
- json-server/node
- Javascript Vanilla - Frontend
  - HTML5, CSS3
- IDE Visual Studio Code

#### 2.5.2 Layout del 'frontend' ####

El 'layout' es muy básico:
- app
  - home : Pagina principal
  - login : Componente de login
  - modal : Componente con ventanas modales
  - ... (resto de componentes)
- shared : Servicios core, utilidades y configuración compartida
- index.html

#### 2.5.3 Proceso de desarrollo para un 'Frontend Developer' ####

Hay varias formas de trabajar:

Opción 1: Servidor node con datos de prueba en un JSon (json-server). Para simplificar su ejecución, usar el script:
```
$run-jsonserver.sh --test
```

Opción 2: Servidor node contra un backend previamente instanciado:
```
$run-jsonserver.sh --backend
```

### 2.6 Arquitectura de desarrollo de los módulos de Backend ###
#### 2.6.1 Tecnologías backend ####

- GIT
- maven
- Docker/Compose
- MySQL 5.X
- Java 1.8 - Backend
  - Spring Boot
  - log4j2
  - JSoup
  - Swagger
  - lombok
  - jacoco/surefire/failsafe
  - JWT
- IDE eclipse
- Postman

#### 2.6.2 Layout de la aplicacion backend ####

El backend es una aplicación Java, desarrollada en capas, con el soporte de spring boot. Cada capa es un subproyecto que depende de la anterior de la siguiente forma:

 vws-persistence : Capa de persistencia desarrollada con el soporte de 'spring-data'.
- Utilizamos el soporte de H2 embebido, para la fase de desarrollo y pruebas
- Utilizamos el soporte para mysql dockerizado, para producción

- vws-services: Capa con el modelo y los servicios de negocio.

- vws-resources: Capa de controladores, donde exponemos nuestra API securizada con el soporte de 'spring-security', al mundo. Contiene la infraestructura necesaria para generar el backend de la aplicación Web, soporte para Docker incluido.
  - Securizacion y JWT: La autenticación esta basada en 'token' JWT. El cliente se encarga de enviar en sus peticiones, el 'token' JWT que el servidor le ha proporcionado. El 'token' caduca a los 10 dias


#### 2.6.3 Proceso de desarrollo para un 'Backend Developer' ####

Importar el proyecto como proyecto 'maven' en tu editor favorito. Tenemos soporte para las 'spring-devtools' que nos permiten hacer cambios en caliente muy fácilmente. En mi caso, utilizo Eclipse.

- Aunque no aporta mucho y genera algun [problema](http://stackoverflow.com/questions/31495451/is-there-a-permanent-fix-for-eclipse-deployment-assembly-losing-the-maven-depend), es posible trabajar con un [tomcat desde dentro del eclipse](http://stackoverflow.com/questions/34927236/how-to-run-spring-boot-app-in-eclipse-tomcat)

Tenemos dos perfiles, separados en dos ficheros de configuración 'yml':

- default : Ver fichero 'application.yml' . Se usa para desarrollo

- container : Ver fichero 'application-container.yml'. En general, se ejecuta para entornos de producción o para simular el entorno producción

##### 2.6.3.1 Como probar el API: Ejecución del backend #####

Podemos lanzar nuestro backend de varias formas para probar nuestra API:

- Ejecución desde eclipse: El fichero con la función 'main()' es el  en "ApplicationVWS". Si lo ejecutamos desde nuestro editor, instancia la aplicación backend en el puerto 80: http://localhots:8080 . Si no indicamos nada, se ejecuta con el perfil 'default'.

- Ejecución desde consola contra BBDD H2: Hemos generado un script 'run-springbootapp-with-h2.sh' que fundamentalmente lanza la aplicación spring-boot en http://localhots:8080, utilizando el profile 'default'. 

- Ejecución del contenedor docker con la configuración de producción. Para ello, ejecutamos el comando 'docker-compose up' o bien 'docker-compose up service-vws-backend' si solo queremos instanciar el backend. Tenemos la aplicación corriendo en 'http://localhost:8080'

###### 2.6.3.1.1 Postman ######

Para probar el API, lo mejor es utilizar la herramienta 'postman':
- Importar en el Postman, la colección de peticiones del fichero 'resources/postman/VWS REST API.postman_collection.json'

- Ejecutar un POST contra el servicio /login, para obtener el token que el que haremos el resto de peticiones. 

- Crear un 'Environment' para el VWS y crear las siguientes claaves - valores:
  - server => localhost:8080
  - jwtoken => Token obtenido anteriormente

- Configurar el postman para que todas las peticiones usen ese token en la cabecera y ya podemos lanzar el resto de peticiones

###### 2.6.3.1.2 Curl ######

- Se lanza una petición de login:
```
curl -i -H "Content-Type: application/json" -X POST -d '{ "userName": "rodrigo", "password": "pepe"}' http://localhost:8080/login
```

- Con el token JWT que devuelve la peticion anterior, recuperamos los favoritos del usuario 'rodrigo'

```
curl -H "Authorization: Bearer xxx.yyy.zzz" http://localhost:8080/api/favorites/
```

#### 2.6.4 Test unitarios y de integración en el Backend #####

Hemos utilizado el soporte de spring y Junit, para desarrollar test unitarios y de integración, tratando de emplear TDD en la medida de lo posible.

Hemos tomado como referencia las buenas prácticas detalladas en el portal [www.baeldung.com](
http://www.baeldung.com/spring-boot-testing?utm_content=buffer61c1e&utm_medium=social&utm_source=twitter.com&utm_campaign=buffer), de la propia [documentación de spring](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-testing.html)
 y de [www.springboottutorial.com](http://www.springboottutorial.com/unit-testing-for-spring-boot-rest-services)

#### 2.6.5 Sobre Swagger y la documentacion del API ####

La API Rest. está documentada con el soporte de swagger en la URL siguiente: http://localhost:8080/swagger-ui.html


### 2.7 Arquitectura de desarrollo de la BB.DD ###

La BBDD tiene un modelado muy simple, pues ademas de almacenar los usuarios de la aplicación para su autenticación, cubre la funcionalidad que permite administrar los favoritos del usario autenticado.

El DDL y DML, se encuentran en los ficheros 'sql' del directorio 'vws/vws-persistence/src/main/docker/vws-mysql/src/*.sql'

#### 2.7.1 Sobre el Modelado y su manteniento ####

La técnica de modelado consiste en generar el esquema, a partir de los objetos de negocio: [JPA Database Schema](https://www.thoughts-on-java.org/create-generate-table-model/)

Como queremos que se genere un fichero SQL para MySQL, utilizaremos la configuración de producción.Los pasos para extender el modelo de BBDD serían los siguientes:

- Creamos los objetos del modelo (Account, AccountRepository...) anotándolos adecuadamente con JPA

- Actualizamo en el fichero 'application-container.yml', la opcion de jpa: 
  ```
   jpa:
        database: MYSQL
        hibernate:
            ddl-auto: auto
  ```
 
  ```
   jpa:
        database: MYSQL
        hibernate:
            ddl-auto: create-drop
  ```

- Re-compilamos la aplicacion y generamos los contenedores docker

- Ejecutamos la aplicación como para producción

- Ejecutamos el script 'backup-DDL.sh' que extrae el modelo regenedo a un fichero SQL

- Copiamos el fichero anteriormente generado 
  ```
  $cp 00-vws-ddl.sql src/main/docker/vws-mysql/src/00-vws-ddl.sql
  ```

#### 2.7.2  Como conectarse a H2 Embebida #####

Con la aplicacion iniciada para el entorno de desarrollo, entraríamos en el navegador en la URL: http://localhost:8080/h2

Introducimos la cadena de conexion, usuario y password que vemos en el fichero 'application.yml'

Ejemplo:
```
 user: root
 pass:
 JDBC URL: jdbc:h2:mem:db;DB_CLOSE_DELAY=-1
```

#### 2.7.3 Como conectarse a MySQL dockerizado #####

Con la aplicación iniciada para el entorno de producción, abrimos un terminal y ejecutamos el siguiente comando:

  ```
  $ docker exec -i -t cnt-vws-mysql /bin/bash
  ```

Una vez dentro, simplemente nos conectamos a la BB.DD con el cliente MySQL que proporciona el contenedor:
 
  ```
  $ mysql vws -uroot -proot
  ```

Una segunda opción sería, si tienes un cliente de MySQL instalado en tu maquina HOST, conectarse directamente al servidor MySQL dentro del contenedor,pues está escuchando en el puerto 5306.
  ```
  $ mysql vws -P 5306 -uroot -proot -h 127.0.0.1
  ```
## 3 Nuevas funcionalidades a implementar ##

- Automatización de la descarga de pelis. Por ejemplo, “Reservar Spiderman” y cuando "Spiderman" salga y ademas en la calidad que pongamos, se pondrá a descargar.

- Poner las notas de las pelis:
  - Implementar el parser de filmaffinity o bien http://www.cinesift.com/
  - Usar una API pública de metracritic o similar ( https://www.publicapis.com/ )

## 4 Técnicas/Tecnlogías a investigar ##

- Revisar la configuracion de spring boot y la carga de properties  
  - https://docs.spring.io/spring-boot/docs/current/reference/html/common-application-properties.html
  - http://www.baeldung.com/spring-boot-application-configuration

- Montar el CAS con Soporte para Oauth, JWT...etc e integrarlo
  - En mi Sandbox tengo montado un CAS. Faltaría dockerizarlo
  - Autorizacion con OAuth2: https://spring.io/guides/tutorials/spring-boot-oauth2/
  - Ver esta documentacion: https://spring.io/guides/tutorials/spring-security-and-angular-js/

- Roles a la aplicacion:
  - http://www.baeldung.com/role-and-privilege-for-spring-security-registration
  - https://github.com/spring-guides/tut-bookmarks
  - https://spring.io/guides/tutorials/bookmarks/#_securing_a_rest_service

- Montar un API Gateway/Manager en el que delegar la autenticacion/autorizacion
  - https://getkong.org/about/
  - https://programar.cloud/post/demo-del-api-gateway-kong/
  - https://apiumbrella.io

- Soporte para la nube Spring Cloud
  - Microservicios, Eureka: https://spring.io/blog/2015/07/14/microservices-with-spring
  - Introducción a la base de datos NoSQL Redis 
  - https://goo.gl/JBqiHE

- Soporte para Spring Data Redis - https://goo.gl/oegRqu

- Evitar problemas de concurrencia: Optimistic Lock
  - Charla Gus: https://youtu.be/fZo8Zp2otqQ
  - http://labs.unacast.com/2016/02/25/on-idempotency-in-distributed-rest-apis/
  - https://spring.io/guides/tutorials/bookmarks/
  - Best practices for concurrency control in REST APIs: https://goo.gl/Xqqvii
  - https://stackoverflow.com/questions/30080634/concurrency-in-a-rest-api

- Revisar la configuracion del apache y el tomcat embebidos
  - https://elpesodeloslunes.wordpress.com/2014/09/07/el-servidor-tomcat-desde-cero-3-configuracion-basica/

- Probar mutation Testing
  - https://www.adictosaltrabajo.com/tutoriales/mutation-testing-con-pit/
