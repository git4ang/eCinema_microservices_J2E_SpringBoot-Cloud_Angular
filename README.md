# eCinema Microservice (fullStack Project): Java EE & Angular 11

***
***
Project developed in **Java EE** with **Spring 5 framework** (+ Spring Boot & Spring Cloud) and frontend (Angular 11, bootstrap 4).

- The goal of this project is to implement a Java EE web application on the backend side and Angular 7 on the frontend side for managing the Cinema entity.
  This project concerns the implementation of JPA-2, JUnit-Mockito, Hibernate 6, Spring 5 (MVC, IOC, Spring security, Spring boot, Spring cloud ... etc),
  microservices (config-server, eureka-server, gateway-server, open feign, loadbalancer, Hateoas, actuator...etc), JWT, MySQL Database, H2 Database, GIT, Maven, docker,
  Angular 11, Boostrap 4… etc.
***
## Backend side: Java EE & Spring 5 (Spring Boot 2.50 & Spring Cloud)

### Objective and detail of the project

   - Each **City entity** is defined by his id, a name and his coordinate position.
   - Each **Cinema entity** is defined by his id, name, room entity, City entity
   - Each **movieRoomm entity** is defined by his id, name, number of cinemaTicket, Cinema entity, cinemaTicket entity
   - Each **cinemaTicket entity** is defined by his id, seatNumber, movieRoom
   - Each **Category entity** is defined by his id, name, movies collection
   - Each **Movie entity** is defined by his id, title, releaseDate, runningTime, director, starring, country, language, photo, description.
   - Each **movieProjection entity** is defined by his id, dateProjection, price, Movie entity, movieSession, roomCinema entity, movieTicket entity.
   - Each **movieSession entity** id defined by his id, movieProjection entity.
   - Each **movieTicket entity** is dined by his id, price, codePayment, reserved, cinemaTicket entity, movieProjection entity.
***
### The creation of the project is based on the following points that will be detailed below:
   - [x] Project initial (multiProject)
   ***
   - [ ] M01-config-server module creation.
     - Spring Cloud Config provides server-side and client-side support for externalized configuration in a distributed system.
       With the Config Server, you have a central place to manage external properties for applications across all environments.
   ***
   - [ ] M02-config-eureka module creation.
     - Eureka Server is an application that holds the information about all client-service applications.
       Every Micro service will register into the Eureka server and Eureka server knows all the client applications running on each port and IP address.
   ***  
   - [ ] M03-config-gateway module creation.
     - An API Gateway is a server that is the single entry point into the system. All requests from clients first go through the API Gateway.
       It then routes requests to the appropriate microservice.
   ***  
   - [ ] M10-microservice-city module creation.
     - Set up the M10-city-microservice module with the creation of the necessary packages such as: entities, repositories, restControllers, services, exceptions, proxies ... etc
   ***
   - [ ] M11-microservice-cinema module creation.
     - Set up the M11-cinema-microservice module with the creation of the necessary packages such as: entities, repositories, restControllers, services, exceptions, proxies ... etc
   ***
   - [ ] M12-microservice-movie module creation.
     - Set up the M12-movie-microservice module with the creation of the necessary packages such as: entities, repositories, restControllers, services, exceptions, proxies ... etc
   ***
  - [ ] M13-microservice-ticket module creation.
    - Set up the M13-ticket-microservice module with the creation of the necessary packages such as: entities, repositories, restControllers, services, exceptions, proxies ... etc
  ***
  - [ ] **Zipkin server** and **Sleuth** for all microservices modules.
    - [ ] **M10-city-microservice**
    - [ ] **M11-cinema-microservice**
    - [ ] **M12-movie-microservice**
    - [ ] **M13-ticket-microservice**
    - **Docker:** docker run -d -p 9411:9411 openzipkin/zipkin
    - **Java:** curl -sSL https://zipkin.io/quickstart.sh | bash -s java -jar zipkin.jar
  ***    
  - [ ] Swagger2 config for all microservices modules.
    - [ ] **M10-city-microservice**
    - [ ] **M11-cinema-microservice**
    - [ ] **M12-movie-microservice**
    - [ ] **M13-ticket-microservice**
      - [**Swagger-url employee**] (http://localhost:8080/employee-microservice/swagger-ui/)
  ***    
   - [ ] Unit Tests for the module **M10-city-microservice**.
   - [ ] Unit Tests for the module **M11-cinema-microservice**.
   - [ ] Unit Tests for the module **M12-movie-microservice**.
   - [ ] Unit Tests for the module **M13-ticket-microservice**.
   ***
   - [ ] Validations JPA for the module **M10-city-microservice**.
   - [ ] Validations JPA for the module **M11-cinema-microservice**.
   - [ ] Validations JPA for the module **M12-movie-microservice**.
   - [ ] Validations JPA for the module **M13-ticket-microservice**.
   ***
   - [ ] Spring Security for the **M01-config-server** 
   - [ ] Spring Security for the **M02-eureka-server** 
   - [ ] Spring Security for the **M03-gateway-server** 
   - [ ] Spring Security for the **M10-city-server** 
   - [ ] Spring Security for the **M11-cinema-server** 
   - [ ] Spring Security for the **M12-movie-server** 
   - [ ] Spring Security for the **M13-ticket-server**
   ***
   - [ ] JWT Json Web Token for the eCinema project 
    
***
***
## Frontend side: Angular 11 & Boostrap 4
***
***

    
    