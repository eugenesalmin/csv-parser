In order see if the app working:

- run 'mvn clean install' inside the project's folder
- put 'csv-analyzer.war' from 'target' folder to tomcat webapps directory
- open root context of the app in browser and provide with 'folder' param existing and readable directory pathname
E.g. http://localhost:8080/csv-analyzer/?folder=d:/Temp 
