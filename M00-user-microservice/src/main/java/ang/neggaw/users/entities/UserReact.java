package ang.neggaw.users.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author ANG
 * @since 12-07-2021 17:07
 */

@Document(collection = "tb_users")
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
@Builder
public class UserReact implements Serializable {

    @Id
    @Indexed(unique = true)
    @MongoId(FieldType.INT64)
    private String idUser;

    @NonNull
    @NotNull(message = "username field cannot be null")
    @Indexed(unique = true)
    private String username;

    @NonNull
    @NotNull(message = "password field cannot be null")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ToString.Exclude
    private String password;

    @NonNull
    @NotNull(message = "rePassword field cannot be null")
    @Transient
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ToString.Exclude
    private String rePassword;

    @NonNull
    @Email(regexp=".*@.*\\..*", message = "Email should be valid")
    @Indexed(unique = true)
    private String email;

    private Boolean enabled;

    private Date lastPasswordResetDate;

    private List<String> authorities = new ArrayList<>();

    @NonNull
    @NotNull(message = "Customer field cannot be null")
    @JsonIgnoreProperties(value = {"users"})
    private CustomerReact customer;

    @NonNull
    @NotNull(message = "idRole field cannot be null")
    @Positive
    @Transient
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ToString.Exclude
    private String idRole;

    private EntityState entityState;

    // Additional information
    public enum EntityState { CREATED, UPDATED, DELETED, PROCESSING }

}
