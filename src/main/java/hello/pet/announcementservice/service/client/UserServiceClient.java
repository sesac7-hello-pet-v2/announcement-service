package hello.pet.announcementservice.service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service")
public interface UserServiceClient {
    @GetMapping("/internal/users/{id}/nickname")
    String getNickname(@PathVariable("id") Long userId);
}
