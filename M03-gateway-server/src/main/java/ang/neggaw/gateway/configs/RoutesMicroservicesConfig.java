package ang.neggaw.gateway.configs;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RoutesMicroservicesConfig {

    // dynamics routes configuration
//    @Bean
//    DiscoveryClientRouteDefinitionLocator dynamicsRoutes(ReactiveDiscoveryClient discoveryClient,
//                                                         DiscoveryLocatorProperties locatorProperties) {
//        return new DiscoveryClientRouteDefinitionLocator(discoveryClient, locatorProperties);
//    }

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder routeLocatorBuilder) {
        return routeLocatorBuilder.routes()
                .route("cityRoute", predicate -> predicate.path("/api/cities/**").uri("LB://CITY-MICROSERVICE"))

                .route("cinemaRoute", predicate -> predicate.path("/api/cinemas/**").uri("LB://CINEMA-MICROSERVICE"))
                .route("roomRoute", predicate -> predicate.path("/api/rooms/**").uri("LB://CINEMA-MICROSERVICE"))
                .route("sessionRoute", predicate -> predicate.path("/api/seats/**").uri("LB://CINEMA-MICROSERVICE"))

                .route("categoryRoute", predicate -> predicate.path("/api/categories/**").uri("LB://MOVIE-MICROSERVICE"))
                .route("movieRoute", predicate -> predicate.path("/api/movies/**").uri("LB://MOVIE-MICROSERVICE"))
                .route("projectionRoute", predicate -> predicate.path("/api/projections/**").uri("LB://MOVIE-MICROSERVICE"))
                .route("sessionRoute", predicate -> predicate.path("/api/sessions/**").uri("LB://MOVIE-MICROSERVICE"))

                .route("ticketRoute", predicate -> predicate.path("/api/tickets/**").uri("LB://TICKET-MICROSERVICE"))

                .route("customerRoute", predicate -> predicate.path("/api/customers/**").uri("LB://USER-MICROSERVICE"))
                .route("roleRoute", predicate -> predicate.path("/api/roles/**").uri("LB://USER-MICROSERVICE"))
                .route("userRoute", predicate -> predicate.path("/api/users/**").uri("LB://USER-MICROSERVICE"))

                .build();
    }
}
