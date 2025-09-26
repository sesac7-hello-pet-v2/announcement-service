package hello.pet.announcementservice.service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "application-service")
public interface ApplicationServiceClient {
    @GetMapping("/internal/applications/exists")
    boolean existsByAnnouncementIdAndApplicantId(
            @RequestParam("announcementId") Long announcementId,
            @RequestParam("applicantId") Long applicantId
    );
}
