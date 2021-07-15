package ang.neggaw.users.reactiveSecurities.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ANG
 * @since 13-07-2021 11:56
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyHttpResponse {
    private int code;
    private String message;
    private Object data;
}