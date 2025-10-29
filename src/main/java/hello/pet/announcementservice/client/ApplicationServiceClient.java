package hello.pet.announcementservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
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

    /**
     * 공고 마감 시 해당 공고의 모든 신청 상태를 UNDER_REVIEW로 변경
     * @param announcementId 마감된 공고 ID
     */
    @PutMapping("/announcement/{announcementId}/close")
    void updateApplicationsToUnderReviewForClosedAnnouncement(@PathVariable("announcementId") Long announcementId);
}
