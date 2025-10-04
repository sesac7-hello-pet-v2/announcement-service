package hello.pet.announcementservice.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PetResponse {
    private Long id;
    private String animalType;
    private String breed;
    private String gender;
    private String health;
    private String personality;
    private Integer age;
    private String imageUrl;
    private Boolean announced;
}