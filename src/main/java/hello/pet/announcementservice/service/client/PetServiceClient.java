package hello.pet.announcementservice.service.client;

import hello.pet.announcementservice.dto.response.PetResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "pet-service")
public interface PetServiceClient {

    @GetMapping("/pets/{petId}")
    PetResponse getPet(@PathVariable Long petId);
}
