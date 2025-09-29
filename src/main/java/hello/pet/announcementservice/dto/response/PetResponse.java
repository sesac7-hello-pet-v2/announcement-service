package hello.pet.announcementservice.dto.response;

import lombok.Data;

@Data
public class PetResponse {
    private Long id;
    private String animalType;  // Pet Service에서 전달받는 String 값
    private String breed;
    private String gender;
    private String health;
    private String personality;
    private Integer age;
    private String imageUrl;
}