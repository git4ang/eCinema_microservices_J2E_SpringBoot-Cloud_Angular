package ang.neggaw.users.services;

import ang.neggaw.users.entities.UserReact;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @author ANG
 * @since 12-07-2021 17:09
 */

public interface UserService {
    Mono<UserReact> createUser(UserReact userReact);
    Mono<UserReact> getUserById(String idUser);
    Mono<UserReact> getUserByUsername(String username);
    Mono<UserReact> getUserByEmail(String email);
    Flux<UserReact> allUsers();
    Flux<UserReact> allUsersByIds(List<String> usersIds);
    Mono<UserReact> updateUser(String idUser, UserReact userReact);
    Mono<UserReact> addOrRemoveUserToRole(String idUser, String idRole, boolean isRemoved);
    Mono<UserReact> deleteUserById(String idUser);
    Mono<UserReact> deleteUserByUsername(String username);
}
