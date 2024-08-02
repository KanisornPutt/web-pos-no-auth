package authentication.example.authentication.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StoreRequest {

    private String name;

    private String storeCode;

    private String password;

    private String promptpayId;

}
