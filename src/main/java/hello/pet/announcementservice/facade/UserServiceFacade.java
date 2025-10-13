package hello.pet.announcementservice.facade;

import hello.pet.announcementservice.client.UserServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserServiceFacade {

    private final UserServiceClient userServiceClient;

    public String getShelterName(Long userId) {
        return userServiceClient.getNickname(userId);
    }
}
