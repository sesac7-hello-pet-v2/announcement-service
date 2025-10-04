package hello.pet.announcementservice.dto.response;

import hello.pet.announcementservice.entity.Announcement;
import hello.pet.announcementservice.entity.AnnouncementStatus;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnnouncementDetailResponse {

    private Long id;
    private String breed;
    private String gender;
    private String health;
    private String personality;
    private int age;
    private String shelterName;
    private LocalDateTime createdAt;
    private LocalDateTime endDate;
    private String imageUrl;
    private AnnouncementStatus announcementStatus;
    private String animalType;
    private boolean alreadyApplied;

    public static AnnouncementDetailResponse from(
            Announcement announcement,
            PetResponse pet,
            String shelterName,
            boolean alreadyApplied
    ) {
        return AnnouncementDetailResponse.builder()
                                         .id(announcement.getId())
                                         .breed(pet.getBreed())
                                         .gender(pet.getGender())
                                         .health(pet.getHealth())
                                         .personality(pet.getPersonality())
                                         .age(pet.getAge())
                                         .shelterName(shelterName)
                                         .createdAt(announcement.getCreatedAt())
                                         .endDate(announcement.getEndDate())
                                         .imageUrl(pet.getImageUrl())
                                         .announcementStatus(announcement.getStatus())
                                         .animalType(pet.getAnimalType())
                                         .alreadyApplied(alreadyApplied)
                                         .build();
    }
}

