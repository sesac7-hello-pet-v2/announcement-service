package hello.pet.announcementservice.facade;

import feign.FeignException;
import hello.pet.announcementservice.dto.response.PetResponse;
import hello.pet.announcementservice.client.PetServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PetServiceFacade {

    private final PetServiceClient petServiceClient;

    public PetResponse getPet(Long petId) {
        return petServiceClient.getPet(petId);
    }

    public void markAsAnnounced(Long petId) {
        try {
            petServiceClient.markAsAnnounced(petId);
        } catch (FeignException.Conflict e) {
            throw new IllegalStateException("해당 펫은 이미 다른 공고에 등록되어 있습니다.");
        } catch (FeignException e) {
            throw new IllegalStateException("펫 상태 업데이트 중 오류가 발생했습니다: " + e.getMessage(), e);
        }
    }
}
