package ang.neggaw.users.services.implementations;

import ang.neggaw.users.entities.RoleReact;
import ang.neggaw.users.repositories.RoleRepository;
import ang.neggaw.users.services.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
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
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public Mono<RoleReact> createRole(RoleReact role) {

        return roleRepository.findById(role.getIdRole())
                .flatMap(___ -> Mono.error(new ResponseStatusException(HttpStatus.CONFLICT, String.format("Role with idRole: '%s' already exists.", role.getIdRole()))))
                .cast(RoleReact.class)

                .switchIfEmpty(Mono.defer(() -> roleRepository.findByRoleName(role.getRoleName())
                        .flatMap(___ -> Mono.error(new ResponseStatusException(HttpStatus.CONFLICT, String.format("Role with roleName: '%s' already exists.", role.getRoleName()))))))
                .switchIfEmpty( Mono.defer(() -> {
                    role.setEntityState(RoleReact.EntityState.CREATED);
                    return roleRepository.save(role);
                }));
    }

    @Override
    public Mono<RoleReact> getRoleById(String idRole) {
        return roleRepository.findById(idRole);
    }

    @Override
    public Mono<RoleReact> getRoleByRoleName(String roleName) {
        return roleRepository.findByRoleName(roleName);
    }

    @Override
    public Flux<RoleReact> allRoles() {
        return roleRepository.findAll();
    }

    @Override
    public Flux<RoleReact> allRolesByIds(List<String> rolesIds) {
        return Flux.fromIterable(rolesIds)
                .parallel()
                .runOn(Schedulers.boundedElastic())
                .flatMap(roleRepository::findById)
                .ordered(Comparator.comparing(RoleReact::hashCode));
    }

    @Override
    public Mono<RoleReact> updateRole(String idRole, RoleReact roleReact) {
        return roleRepository.findById(idRole)
                .flatMap(roleDB -> {
                    roleReact.setEntityState(RoleReact.EntityState.UPDATED);
                    BeanUtils.copyProperties(roleReact, roleDB);
                    return roleRepository.save(roleDB);
                });
    }

    @Override
    public Mono<RoleReact> deleteRoleById(String idRole) {
        return roleRepository.findById(idRole)
                .flatMap(roleDB -> {
                    roleDB.setEntityState(RoleReact.EntityState.DELETED);
                    return roleRepository.delete(roleDB)
                            .then(Mono.just(roleDB));
                });
    }

    @Override
    public Mono<RoleReact> deleteRoleByRoleName(String roleName) {
        return roleRepository.findByRoleName(roleName)
                .flatMap(roleDB -> {
                    roleDB.setEntityState(RoleReact.EntityState.DELETED);
                    return roleRepository.delete(roleDB)
                            .then(Mono.just(roleDB));
                });
    }
}
