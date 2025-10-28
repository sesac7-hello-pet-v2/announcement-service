package hello.pet.announcementservice.facade;

import hello.pet.announcementservice.client.ApplicationServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ApplicationServiceFacade {

    private final ApplicationServiceClient applicationServiceClient;

    public boolean hasUserAppliedToAnnouncement(Long announcementId, Long userId) {
        return applicationServiceClient.hasUserAppliedToAnnouncement(announcementId, userId);
    }

    public void updateApplicationsToUnderReviewForClosedAnnouncement(Long announcementId) {
        applicationServiceClient.updateApplicationsToUnderReviewForClosedAnnouncement(announcementId);
    }
}
