package hello.pet.announcementservice.facade;

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
        petServiceClient.markAsAnnounced(petId);
    }
}
