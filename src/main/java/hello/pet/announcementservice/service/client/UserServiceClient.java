package hello.pet.announcementservice.service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "user-service",
        url = "http://user-service:8082",
        path = "/internal/v1/users"
)
public interface UserServiceClient {

    @GetMapping("/{id}/nickname")
    String getNickname(@PathVariable("id") Long userId);
}
