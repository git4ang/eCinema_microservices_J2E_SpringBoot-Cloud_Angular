package ang.neggaw.cinemas.securities.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ANG
 * @since 15-07-2021 19:59
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyHttpResponse {
    private int code;
    private String message;
    private Object data;
}