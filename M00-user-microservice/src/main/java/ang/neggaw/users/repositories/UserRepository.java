package ang.neggaw.users.repositories;

import ang.neggaw.users.entities.UserReact;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

/**
 * @author ANG
 * @since 12-07-2021 17:08
 */

public interface UserRepository extends ReactiveMongoRepository<UserReact, String> {
    Mono<UserReact> findByUsername(String username);
    Mono<UserReact> findByEmail(String email);
}
