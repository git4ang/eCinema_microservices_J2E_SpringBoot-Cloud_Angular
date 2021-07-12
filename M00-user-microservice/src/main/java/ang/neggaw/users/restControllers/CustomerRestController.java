package ang.neggaw.users.restControllers;

import ang.neggaw.users.entities.CustomerReact;
import ang.neggaw.users.services.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.List;

/**
 * @author ANG
 * @since 12-07-2021 17:11
 */

@EnableCaching
@Log4j2
@RequestMapping(value = {"/api/customers" })
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RestController
public class CustomerRestController {

    private final CustomerService customerService;

    @PostMapping
    public Mono<Object> createCustomer(@Valid @RequestBody CustomerReact customer) {
        return customerService.createCustomer(customer)
                .map(c -> {
                    log.info("Customer with id: '{}' CREATED successfully", c.getIdCustomer());
                    return ResponseEntity.status(HttpStatus.CREATED).body(c);
                })
                .cast(Object.class)
                .onErrorResume(e -> {
                    log.error(e.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage()));
                });
    }

    @GetMapping(value = "/{idCustomer}")
    public Mono<Object> getCustomerById(@PathVariable(value = "idCustomer") String idCustomer) {
        return customerService.getCustomerById(idCustomer)
                .map(c -> ResponseEntity.status(HttpStatus.OK).body(c))
                .cast(Object.class)
                .onErrorResume(e -> {
                    log.error(e.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage()));
                });
    }

    @GetMapping(value = "/names/{name}")
    public Flux<Object> allCustomersByName(@PathVariable(value = "name") String name) {
        return customerService.allCustomersByName(name)
                .map(cs -> ResponseEntity.status(HttpStatus.OK).body(cs))
                .cast(Object.class)
                .onErrorResume(e -> {
                    log.error(e.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.NO_CONTENT).body(e.getMessage()));
                });
    }

    @PostMapping(value = "/multiCustomers/ids")
    public Flux<Object> allCustomersByIds(@RequestBody List<String> customersIds) {
        return customerService.allCustomersByIds(customersIds)
                .map(cs -> ResponseEntity.status(HttpStatus.OK).body(cs))
                .cast(Object.class)
                .onErrorResume(e -> {
                    log.error(e.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.NO_CONTENT).body(e.getMessage()));
                });
    }

    @GetMapping
    public Flux<Object> allCustomers() {
        return customerService.allCustomers()
                .map(cs -> ResponseEntity.status(HttpStatus.OK).body(cs))
                .cast(Object.class)
                .onErrorResume(e -> {
                    log.error(e.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.NO_CONTENT).body(e.getMessage()));
                });
    }

    @PutMapping(value = "/{idCustomer}")
    public Mono<Object> updateCustomer(@PathVariable(value = "idCustomer") String idCustomer,
                                                              @Valid @RequestBody CustomerReact customerReact) {
        return customerService.updateCustomer(idCustomer, customerReact)
                .map(c -> {
                    log.info("Customer with id: '{}' UPDATED successfully", idCustomer);
                    return ResponseEntity.status(HttpStatus.OK).body(c);
                })
                .cast(Object.class)
                .onErrorResume(e -> {
                    log.error(e.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage()));
                });
    }

    @DeleteMapping(value = "/{idCustomer}")
    public Mono<Object> deleteCustomer(@PathVariable(value = "idCustomer") String idCustomer) {
        return customerService.deleteCustomer(idCustomer)
                .map(customerDB -> {
                    log.info("Customer with id: '{}' DELETED successfully", idCustomer);
                    return ResponseEntity.status(HttpStatus.ACCEPTED).body(customerDB);
                })
                .cast(Object.class)
                .onErrorResume(e -> {
                    log.error(e.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage()));
                });
    }
}
