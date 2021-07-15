package ang.neggaw.users.services.implementations;

import ang.neggaw.users.entities.CustomerReact;
import ang.neggaw.users.repositories.CustomerRepository;
import ang.neggaw.users.services.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Comparator;
import java.util.List;

/**
 * @author ANG
 * @since 12-07-2021 17:10
 */

@Log4j2
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Override
    public Mono<CustomerReact> createCustomer(CustomerReact customerReact) {
        customerReact.setEntityState(CustomerReact.EntityState.CREATED);
        return customerRepository.save(customerReact);
    }

    @Override
    public Mono<CustomerReact> getCustomerById(String idCustomer) {
        return customerRepository.findById(idCustomer);
    }

    @Override
    public Flux<CustomerReact> allCustomersByName(String name) {
        return customerRepository.findByName(name);
    }

    @Override
    public Flux<CustomerReact> allCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public Flux<CustomerReact> allCustomersByIds(List<String> customersIds) {
        return Flux.fromIterable(customersIds)
                .parallel()
                .runOn(Schedulers.boundedElastic())
                .flatMap(customerRepository::findById)
                .ordered(Comparator.comparing(CustomerReact::hashCode));
    }

    @Override
    public Mono<CustomerReact> updateCustomer(String idCustomer, CustomerReact customerReact) {
        return customerRepository.findById(idCustomer)
                .flatMap(customerDB -> {
                    customerReact.setEntityState(CustomerReact.EntityState.UPDATED);
                    BeanUtils.copyProperties(customerReact, customerDB);
                    return customerRepository.save(customerDB);
                });
    }

    @Override
    public Mono<CustomerReact> deleteCustomer(String idCustomer) {
        return customerRepository.findById(idCustomer)
                .flatMap(customerDB -> {
                    customerDB.setEntityState(CustomerReact.EntityState.DELETED);
                    return customerRepository
                            .delete(customerDB)
                            .then(Mono.just(customerDB));
                });
    }
}
