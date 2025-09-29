package hello.pet.announcementservice.dto.response;

import hello.pet.announcementservice.entity.AnimalType;
import lombok.Data;

@Data
public class PetResponse {
    private Long id;
    private AnimalType animalType;
    private String breed;
    private String gender;
    private String health;
    private String personality;
    private Integer age;
    private String imageUrl;
}