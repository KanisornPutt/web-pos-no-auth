package authentication.example.authentication.dto;

import authentication.example.authentication.entities.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long userId;
    private String firstname;
    private String lastname;
    private String showName;
    private String imageUrl;
    private String email;
    private Long storeId;
    private Role role;
}
