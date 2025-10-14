package hello.pet.announcementservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
