# country-application

Each folder is a separated project.
 * config-service is a cloud configuration that should have all configs to other services
 * country-client is a Angular FE project to display countries data fetched from country-service
 * country-service is a Spring Webflux app that handle data request from client, and call external service to fetch data related.
 * registry-service is a Eureka server that helps monitoring services health, status, downtime, etc...
 
## Start all the services to have a full application running.
 * `mvn install && mvn spring-boot:run` for config, country, registry service. The order is not important, but should be registry -> config -> country service
 * `npm install` or `yarn install` to install packages, then ng serve` for country-client
 
## Discovering API
 * After all the services are up running, navigate to `localhost://4200` to make some API requests.
 * Navigate to `localhost://8761` to see Eureka server running and listed service.
 * Navigate to `http://localhost:8080/swagger-ui/index.html` to see API documentation.
 * Send request to `http://localhost:8888/default/country-service` to get application properties, `curl -X GET http://localhost:8888/country-service/default`
