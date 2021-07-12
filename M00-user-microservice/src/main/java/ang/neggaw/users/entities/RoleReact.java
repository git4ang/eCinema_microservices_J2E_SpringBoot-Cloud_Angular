package ang.neggaw.users.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ANG
 * @since 12-07-2021 17:07
 */

@Document(value = "tb_roles")
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
@Builder
public class RoleReact implements Serializable {

    @Id
    @Indexed(unique = true)
    @MongoId(FieldType.INT64)
    private String idRole;

    @NonNull
    @NotNull(message = "roleName field cannot be null")
    @Indexed(unique = true)
    private String roleName;

    List<String> usernames = new ArrayList<>();

    private EntityState entityState;

    // Additional information
    public enum EntityState { CREATED, UPDATED, DELETED, PROCESSING }

}
