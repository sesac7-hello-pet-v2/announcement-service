package hello.pet.announcementservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "application-service",
        url = "http://application-service:8089",
        path = "/v1/applications"
)
public interface ApplicationServiceClient {
    @GetMapping("/status")
    boolean hasUserAppliedToAnnouncement(
            @RequestParam("announcementId") Long announcementId,
            @RequestParam("userId") Long userId
    );
}
