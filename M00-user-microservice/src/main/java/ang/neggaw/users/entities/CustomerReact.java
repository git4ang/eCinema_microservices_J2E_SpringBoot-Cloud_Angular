package ang.neggaw.users.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ANG
 * @since 12-07-2021 17:06
 */

@Document(value = "tb_customers")
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerReact implements Serializable {

    @Id
    @Field(value = "idCustomer")
    @Indexed(unique = true)
    @MongoId(FieldType.INT64)
    private String idCustomer;

    @NonNull
    @NotNull(message = "customerName field cannot be null")
    private String name;

    @NonNull
    @NotNull(message = "address field cannot be null")
    private String address;

    @NonNull
    @Indexed(unique = true)
    private String phoneNumber;

    private EntityState entityState;

    @JsonIgnoreProperties(value = {"customer"})
    @ToString.Exclude
    private Map<String, String> ids_usernames = new HashMap<>();

    // Additional information
    public enum EntityState { CREATED, UPDATED, DELETED, PROCESSING }
}