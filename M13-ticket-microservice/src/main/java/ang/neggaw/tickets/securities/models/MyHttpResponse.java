package ang.neggaw.tickets.securities.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ANG
 * @since 15-07-2021 21:22
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyHttpResponse {
    private int code;
    private String message;
    private Object data;
}