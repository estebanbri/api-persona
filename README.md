# API-PERSONA
Aplicacion *Spring Boot*.

Comando para ejecutar la aplicacion: 
> mvn clean install spring-boot:run

## POST
    * POST localhost:8080/v1/personas
    * Request Body Ex: {"nombre": "Pepito","apellido": "Perez","edad":30,"sexo":"Masculino"}

## GET
    * GET localhost:8080/v1/personas


## DELETE
    * DELETE localhost:8080/v1/personas/{id}


## PUT
    * PUT localhost:8080/v1/personas/{id}
    * Request Body Ex:{"nombre": "Pepito","apellido": "Perez","edad":30,"sexo":"Masculino"}
