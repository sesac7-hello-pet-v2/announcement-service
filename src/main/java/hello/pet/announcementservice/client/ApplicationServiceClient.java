package hello.pet.announcementservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "application-service",
        url = "${APPLICATION_SERVICE_URL:http://localhost:8089}",
        path = "/v1/applications"
)
public interface ApplicationServiceClient {
    @GetMapping("/status")
    boolean hasUserAppliedToAnnouncement(
            @RequestParam("announcementId") Long announcementId,
            @RequestParam("userId") Long userId
    );
}
