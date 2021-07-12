package ang.neggaw.users.services.implementations;

import ang.neggaw.users.entities.CustomerReact;
import ang.neggaw.users.entities.RoleReact;
import ang.neggaw.users.entities.UserReact;
import ang.neggaw.users.repositories.CustomerRepository;
import ang.neggaw.users.repositories.RoleRepository;
import ang.neggaw.users.repositories.UserRepository;
import ang.neggaw.users.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author ANG
 * @since 12-07-2021 17:11
 */

@Log4j2
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    //private final PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    @Override
    public Mono<UserReact> createUser(UserReact user) {

        return this.userRepository.findById(user.getIdUser())
                .flatMap(___ -> Mono.error(new ResponseStatusException(HttpStatus.CONFLICT, String.format("Unable to create User. User with idUser: '%s' already exists.", user.getIdUser()))))
                .cast(UserReact.class)

                .switchIfEmpty(Mono.defer(() -> userRepository.findByUsername(user.getUsername())
                        .flatMap(___ -> Mono.error(new ResponseStatusException(HttpStatus.CONFLICT, String.format("Unable to create User. User with username: '%s' already exists.", user.getUsername()))))
                        .cast(UserReact.class)

                        .switchIfEmpty(Mono.defer(() -> userRepository.findByEmail(user.getEmail())
                                .flatMap(___ -> Mono.error(new ResponseStatusException(HttpStatus.CONFLICT, String.format("Unable to create User. User with email: '%s' already exists.", user.getEmail()))))
                                .cast(UserReact.class)

                                .switchIfEmpty(Mono.defer(() -> customerRepository.findById(user.getCustomer().getIdCustomer()).cast(CustomerReact.class)
                                        .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Unable to create User. Customer with id: '%s' Not Found.", user.getCustomer().getIdCustomer()))))
                                        .flatMap(c -> {
                                            user.setCustomer(c);
                                            c.setIds_usernames(Map.of(user.getIdUser(), user.getUsername()));
                                            if (user.getPassword().equals(user.getRePassword())) {
                                                user.setPassword(passwordEncoder.encode(user.getPassword()));
                                                user.setEntityState(UserReact.EntityState.CREATED);

                                                user.setLastPasswordResetDate(new Date());
                                                user.setEnabled(true);
                                                return roleRepository.findById(user.getIdRole())
                                                        .log()
                                                        .doOnNext(infoRole -> log.info("Role: {}", infoRole))
                                                        .filter(roleReact -> ! user.getAuthorities().contains(roleReact.getRoleName()))
                                                        .flatMap(roleReact -> {
                                                            log.info("Adding Role with id: '{}' to User with id: '{}'", roleReact.getIdRole(), user.getIdUser());
                                                            roleReact.getUsernames().add(user.getUsername());
                                                            user.setAuthorities(List.of(roleReact.getRoleName()));

                                                            roleRepository.save(roleReact);
                                                            customerRepository.save(c);
                                                            return userRepository.save(user);
                                                        });
                                            } else
                                                return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Passwords are not same"));
                                        })))))));
    }

    @Override
    public Mono<UserReact> getUserById(String idUser) {
        return userRepository.findById(idUser)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("User with id: '%s' Not Found.", idUser))));
    }

    @Override
    public Mono<UserReact> getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("User with username: '%s' Not Found.", username))));
    }

    @Override
    public Mono<UserReact> getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("User with email: '%s' Not Found.", email))));
    }

    @Override
    public Flux<UserReact> allUsers() {
        return userRepository.findAll()
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "No content. Without users in DB.")));
    }

    @Override
    public Flux<UserReact> allUsersByIds(List<String> usersIds) {
        return Flux.fromIterable(usersIds)
                .parallel()
                .runOn(Schedulers.boundedElastic())
                .flatMap(userRepository::findById)
                .ordered(Comparator.comparing(UserReact::hashCode));
    }

    @Override
    public Mono<UserReact> updateUser(String idUser, UserReact userReact) {
        return userRepository.findById(idUser)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Unable to update. User with id: '%s' Not Found.", idUser))))
                .flatMap(userDB -> {
                    userReact.setEntityState(UserReact.EntityState.UPDATED);
                    BeanUtils.copyProperties(userReact, userDB);
                    return userRepository.save(userDB);
                });
    }

    @Override
    public Mono<UserReact> addOrRemoveUserToRole(String idUser, String idRole, boolean isRemoved) {

        return userRepository.findById(idUser)
                .flatMap(user -> roleRepository.findById(idRole)
                        .flatMap(role -> {
                            if(isRemoved && user.getAuthorities().contains(role.getRoleName())) {
                                user.getAuthorities().removeIf(s -> s.equals(role.getRoleName()));
                                role.getUsernames().removeIf(s -> s.equals(user.getUsername()));
                            } else if ( !isRemoved && !user.getAuthorities().contains(role.getRoleName())) {
                                user.getAuthorities().add(role.getRoleName());
                                role.getUsernames().add(user.getUsername());
                            }

                            role.setEntityState(RoleReact.EntityState.UPDATED);
                            roleRepository.save(role);

                            user.setEntityState(UserReact.EntityState.UPDATED);
                            return userRepository.save(user);
                        })
                        .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Role with id: '%s' Not Found.", idRole)))))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("User with id: '%s' Not Found.", idUser))));
    }

    @Override
    public Mono<UserReact> deleteUserById(String idUser) {
        return userRepository.findById(idUser)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Unable to delete. User with id: '%s' Not Found.", idUser))))
                .flatMap(userDB -> {
                    userDB.setEntityState(UserReact.EntityState.DELETED);
                    return userRepository.delete(userDB)
                            .then(Mono.just(userDB));
                });
    }

    @Override
    public Mono<UserReact> deleteUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Unable to delete. User with username: '%s' Not Found.", username))))
                .flatMap(userDB -> {
                    userDB.setEntityState(UserReact.EntityState.DELETED);
                    return userRepository.delete(userDB)
                            .then(Mono.just(userDB));
                });
    }
}
