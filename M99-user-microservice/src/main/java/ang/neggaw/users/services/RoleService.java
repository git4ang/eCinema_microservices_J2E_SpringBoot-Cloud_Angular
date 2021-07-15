package ang.neggaw.users.services;

import ang.neggaw.users.entities.RoleReact;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @author ANG
 * @since 12-07-2021 17:09
 */

public interface RoleService {
    Mono<RoleReact> createRole(RoleReact roleReact);
    Mono<RoleReact> getRoleById(String idRole);
    Mono<RoleReact> getRoleByRoleName(String roleName);
    Flux<RoleReact> allRoles();
    Flux<RoleReact> allRolesByIds(List<String> rolesIds);
    Mono<RoleReact> updateRole(String idRole, RoleReact roleReact);
    Mono<RoleReact> deleteRoleById(String idRole);
    Mono<RoleReact> deleteRoleByRoleName(String roleName);
}
