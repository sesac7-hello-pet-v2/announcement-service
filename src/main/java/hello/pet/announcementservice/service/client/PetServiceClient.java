package hello.pet.announcementservice.service.client;

import hello.pet.announcementservice.dto.response.PetResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "pet-service")
public interface PetServiceClient {

    @GetMapping("/v1/pets/{petId}")
    PetResponse getPet(@PathVariable("petId") Long petId);

    @PatchMapping("/v1/pets/{petId}/mark-announced")
    void markAsAnnounced(@PathVariable("petId") Long petId);
}
