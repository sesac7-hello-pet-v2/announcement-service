package hello.pet.announcementservice.service.facade;

import hello.pet.announcementservice.service.client.UserServiceClient;
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
