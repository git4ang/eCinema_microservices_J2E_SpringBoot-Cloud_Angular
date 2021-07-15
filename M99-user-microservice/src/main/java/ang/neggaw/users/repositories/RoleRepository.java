package ang.neggaw.users.repositories;

import ang.neggaw.users.entities.RoleReact;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

/**
 * @author ANG
 * @since 12-07-2021 17:08
 */

public interface RoleRepository extends ReactiveMongoRepository<RoleReact, String> {
    Mono<RoleReact> findByRoleName(String roleName);
}
