package authentication.example.authentication.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StoreAdminDto {

    private Long id;
    private String name;
    private List<String> categories;
    private String storeCode;
    private String promptpayId;

}
