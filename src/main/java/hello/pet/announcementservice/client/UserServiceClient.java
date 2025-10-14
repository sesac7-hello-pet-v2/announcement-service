package hello.pet.announcementservice.client;

import hello.pet.announcementservice.dto.response.UserDetailResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "user-service",
        url = "http://user-service:8082",
        path = "/internal/v1/users"
)
public interface UserServiceClient {

    @GetMapping("/{userId}")
    UserDetailResponse getUserDetail(@PathVariable("userId") Long userId);
}
