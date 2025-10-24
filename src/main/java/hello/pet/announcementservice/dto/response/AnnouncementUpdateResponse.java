package hello.pet.announcementservice.dto.response;

import hello.pet.announcementservice.entity.Announcement;
import hello.pet.announcementservice.entity.AnnouncementStatus;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class AnnouncementUpdateResponse {
    private Long id;
    private String breed;
    private String gender;
    private String health;
    private String personality;
    private int age;
    private String imageUrl;
    private String animalType;  // Pet Service에서 전달받는 String 값
    private AnnouncementStatus status;
    private LocalDateTime updatedAt;
    private String message;

    public static AnnouncementUpdateResponse from(Announcement announcement, PetResponse pet) {
        return AnnouncementUpdateResponse.builder()
                .id(announcement.getId())
                .breed(pet.getBreed())
                .gender(pet.getGender())
                .health(pet.getHealth())
                .personality(pet.getPersonality())
                .age(pet.getAge())
                .imageUrl(pet.getImageUrl())
                .animalType(pet.getAnimalType())
                .status(announcement.getStatus())
                .updatedAt(announcement.getUpdatedAt())
                .message("입양 공고가 성공적으로 수정되었습니다.")
                .build();
    }
}