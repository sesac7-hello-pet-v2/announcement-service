package hello.pet.announcementservice.facade;

import hello.pet.announcementservice.client.UserServiceClient;
import hello.pet.announcementservice.dto.response.UserDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserServiceFacade {

    private final UserServiceClient userServiceClient;

    public String getNickname(Long userId) {
        UserDetailResponse userDetail = userServiceClient.getUserDetail(userId);
        return userDetail.getNickname();
    }
}
