package hello.pet.announcementservice.service.facade;

import hello.pet.announcementservice.service.client.ApplicationServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ApplicationServiceFacade {

    private final ApplicationServiceClient applicationServiceClient;

    public boolean existsByAnnouncementIdAndApplicantId(Long announcementId, Long applicantId) {
        return applicationServiceClient.existsByAnnouncementIdAndApplicantId(announcementId, applicantId);
    }
}
