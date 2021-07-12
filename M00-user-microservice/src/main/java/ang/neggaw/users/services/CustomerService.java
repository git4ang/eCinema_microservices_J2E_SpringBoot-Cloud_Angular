package ang.neggaw.users.services;

import ang.neggaw.users.entities.CustomerReact;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @author ANG
 * @since 12-07-2021 17:09
 */

public interface CustomerService {
    Mono<CustomerReact> createCustomer(CustomerReact customerReact);
    Mono<CustomerReact> getCustomerById(String idCustomer);
    Flux<CustomerReact> allCustomersByName(String name);
    Flux<CustomerReact> allCustomers();
    Flux<CustomerReact> allCustomersByIds(List<String> customersIds);
    Mono<CustomerReact> updateCustomer(String idCustomer, CustomerReact customerReact);
    Mono<CustomerReact> deleteCustomer(String idCustomer);
}
