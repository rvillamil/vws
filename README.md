## VWS: Video websites scraper ##

Web scraping application for video websites


(La planteamos como no solo de torrent asi tiene mas futuro..) Aplicacion web que realiza el web scraping de paginas con torrents de video.
Para su ejecución, tienes que hacer lo siguiente:
- clonar el repo
- docker run ... docker-compose ...

## Informacion para desarrolladores ##
La aplicacion está desarrollada utilizando el soporte de spring boot, por tanto
está preparada para generar un jar autoejecutable con un tomcat
embebido aunque tambien se puede desplegar en un servidor de aplicaciones con soporte
para Servlet 3.X (no se requiere un web.xml)

## Compilacion y ejecucion basica  ##
 * Levantar el soporte para docker. El entorno de desarrollo/ejecucion
   requiere docker 1.12 o superior
 * Ejecutar un mvn install para que compile y genere todos los contenedores
 * Entrar dentro del proyecto 'vws-docker-support' y ejecutar 'docker-compose up'
 * Ver que la apliacion está iniciada correctamente en:

		- http://localhost:8383/vws-resources-1.0-SNAPSHOT/billboardfilms
		- http://localhost:8383/vws-resources-1.0-SNAPSHOT/

 * Parar con crtl-c y ejecutar un 'docker-compose down'

## Entorno de desarrollo  ##


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

Perfiles por defecto: develop + docker-support
es decir: mvn install <--> mvn install -Pdevelop,docker-support

mvn install -P integration,-docker-support:  para el Jenkins

-Pdevelop = Lanza test unitarios
-Pintegration = Lanza test unitarios y de integracion
-Pdocker-support = Ejecuta el docker build en los proyectos con Docker
NOTA: -P-docker-support , evita que se lancen los docker build

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
