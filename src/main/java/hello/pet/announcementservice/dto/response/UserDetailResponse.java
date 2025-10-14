package hello.pet.announcementservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailResponse {
    private String email;
    private String nickname;
    private String username;
    private String address;
    private String profileUrl;
    private String phoneNumber;
}
