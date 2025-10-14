package hello.pet.announcementservice.facade;

import hello.pet.announcementservice.client.UserServiceClient;
import hello.pet.announcementservice.dto.response.UserDetailResponse;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserServiceFacade {

    private final UserServiceClient userServiceClient;

    public Optional<String> getNickname(Long userId) {
        UserDetailResponse userDetail = userServiceClient.getUserDetail(userId);
        return Optional.ofNullable(userDetail)
                       .map(UserDetailResponse::getNickname);
    }
}
