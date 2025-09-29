# Music Event Finder

Music Event Finder es una app de recomendación de eventos musicales que ofrece al usuario eventos personalizados en base a sus hábitos de escucha en Spotify.

</div>

## 1. Propuesta de valor
Music Event Finder recomienda una selección personalizada de eventos musicales en base a los artistas más escuchados por el usuario en Spotify. Para ello, se han utilizado las APIs de TicketMaster y Spotify. El objetivo principal de la aplicación es facilitar la búsqueda de eventos de interés para los usuarios finales y ofrecer un backend para desarrolladores listo para integrarse en aplicaciones web o móviles que quieran enriquecer la experiencia musical de sus usuarios.

## 2. Justificación de elección de APIs y estructura del Data Mart
**APIs**:
* Spotify: cobertura global de artistas, géneros y popularidad; permite obtener datos acerca de los hábitos de escucha de los usuarios.
* Ticketmaster: catálogo amplio y estructurado de próximos conciertos, clave para agenda real.

**DataMart**:
Se ha elegido una estructura SQLite para el datamart debido a la simplicidad, rendimiento, bajo mantenimiento y facilidad de uso.

## 3. Compilación y ejecución
Requisitos: 
- JDK 21
- Maven 3.9+
- ActiveMQ en ejecución (https://activemq.apache.org/components/classic/documentation/getting-started)
- API key de TicketMaster (https://developer.ticketmaster.com/products-and-docs/apis/discovery-api/v2)
- API key de usuario de Spotify (https://developer.spotify.com/documentation/web-api)


### 3.1 Introducir las claves de las APIs en los ficheros config.properties de los feeders.

### 3.2 Compilar
```
mvn clean install
```

### 3.3 Ejecutar cada módulo en el orden indicado (desde raíz del proyecto)

business-unit:
```
cd business-unit
mvn exec:java -Dexec.mainClass=es.ulpgc.Main
```
event-store-builder:
```
cd event-store-builder
mvn exec:java -Dexec.mainClass=es.ulpgc.Main
```

spotify-feeder:
```
cd spotify-feeder
mvn exec:java -Dexec.mainClass=es.ulpgc.Main
```

ticketmaster-feeder:
```
cd ticketmaster-feeder
mvn exec:java -Dexec.mainClass=es.ulpgc.Main
```

## 4. Ejemplos de uso (consultas y REST)
### 4.1 Formato de evento (línea .events)
```json
{"ts":[2025,9,19,17,37,30,658094700],"ss":"ticketmaster-feeder","id":"https://www.ticketmaster.co.uk/deftones-birmingham-12-02-2026/event/230062E79CB909FB","name":"Deftones","date":"2026-02-12","venue":"bp pulse LIVE","city":"Birmingham"}
```

### 4.2 Endpoints REST (Business Unit)
| Método | Endpoint | Descripción | Parámetros |
|--------|----------|-------------|------------|
| GET | `/health` | Consulta si la API se encuentra en funcionamiento |-|
| GET | `/stats` | Obtiene la cantidad de artistas y eventos almacenados en el datamart |-|
| GET | `/events/top` | Obtiene eventos encontrados en base a los hábitos de escucha | `limit`|
| GET | `/artists/top` | Obtiene los artistas más escuchados por el usuario | `limit`|
| GET | `/events/{id}` | Obtiene un evento en base a su id|-|
| GET | `/artists/{id}` | Obtiene un artista de entre los más escuchados por el usuario en base a su id|-|
| GET | `/events/search` | Busca eventos que contengan el texto indicado como parámetro |`q`, `limit`|
| GET | `/artists/search` | Busca artistas que contengan el texto indicado como parámetro |`q`, `limit`|


Ejemplo:
```
GET http://localhost:8080/artists/top?limit=10
GET http://localhost:8080/events/search?q=Jazz
```
Respuesta (ejemplo):
```json
[
	{"id": "adiafh82319", "name":"Deftones","popularity":"74","genres":"Metal","followers":"5683293","image_url":"imageurl.jpg"},
]
```

## 5. Arquitectura
### 5.1 Arquitectura de la aplicación
Arquitectura de tipo Event-Driven modular, con feeders independientes cuyos datos son recogidos por los modulos event-store-builder y business-unit, dotando a la aplicación de una estructura escalable y extensible.

<img width="594" height="332" alt="architecture_with_ui_left" src="https://github.com/user-attachments/assets/6f8c312b-3e96-458f-b744-737be1c24f92" />


### 5.2 Arquitectura de la Aplicación (capas internas)
- spotify-feeder: Obtiene datos de artistas, popularidad, géneros y seguidores desde la API de Spotify.

<img width="1538" height="1100" alt="imagen_2025-09-29_130814447" src="https://github.com/user-attachments/assets/bed3680c-c98b-4708-9d41-eebec4dc4752" />

- ticketmaster-feeder: Recopila información de eventos, conciertos, fechas y ubicaciones desde Ticketmaster basado en los hábitos de escucha del usuario.
  
<img width="792" height="636" alt="imagen_2025-09-29_132151443" src="https://github.com/user-attachments/assets/6ba55475-8074-4c28-9c13-02d21b80181f" />

- event-store-builder: Centraliza el almacenamiento y procesamiento de eventos para consultas posteriores.

<img width="410" height="352" alt="image" src="https://github.com/user-attachments/assets/d845a562-4d44-4a7a-b6f6-b4105cc26894" />

- business-unit: Datamart y REST API para comunicarse con otras aplicaciones.

<img src="https://github.com/user-attachments/assets/6ec14321-1177-4dfb-930d-205006830d08">

  
## 6. Principios y patrones de diseño aplicados

| Principio / Patrón | Aplicación |
|--------------------|-----------|
| Single Responsibility (SRP) | Separación feeders / builder / API|
| Open/Closed | Añadir nueva fuente = nuevo feeder implementando mismo contrato de evento |
| Publisher/Subscriber (Messaging) | Feeders publican, builder y business-unit se suscriben via ActiveMQ |
| DTO | Transporta datos entre capas sin exponer la estructura interna de los modelos de dominio |
| Inmutabilidad parcial | Objetos de evento tratados como inmutables tras creación |
| Defensive coding | Validaciones básicas (nulos, fechas) antes de persistir |
| Clean code practices | Se ha mantenido un código legible y coherente |


## 7. Roadmap / mejoras futuras
* Autenticación y rate limiting API.
* Aplicación web desarrollada en la nube.
* Soporte para nuevas fuentes de datos, permitiendo una mayor accesibilidad al añadir nuevas plataformas.
* Escalabilidad y microservicios, optimizando la arquitectura del sistema para garantizar la escalabilidad 

