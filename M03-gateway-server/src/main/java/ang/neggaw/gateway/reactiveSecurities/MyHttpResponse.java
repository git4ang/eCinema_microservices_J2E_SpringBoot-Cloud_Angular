package ang.neggaw.gateway.reactiveSecurities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ANG
 * @since 12-07-2021 14:56
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyHttpResponse {
    private int code;
    private String message;
    private Object data;
}