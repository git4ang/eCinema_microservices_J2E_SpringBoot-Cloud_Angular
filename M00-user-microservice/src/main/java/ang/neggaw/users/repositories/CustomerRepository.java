package ang.neggaw.users.repositories;

import ang.neggaw.users.entities.CustomerReact;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author ANG
 * @since 12-07-2021 17:08
 */

public interface CustomerRepository extends ReactiveMongoRepository<CustomerReact, String> {
    Flux<CustomerReact> findByName(String name);
    Mono<CustomerReact> findByPhoneNumber(String phoneNumber);
}
