package hello.pet.announcementservice.dto.response;

import hello.pet.announcementservice.entity.Announcement;
import hello.pet.announcementservice.entity.AnnouncementStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class AnnouncementListResponse {
    private Long id;
    private String breed;
    private String imageUrl;
    private AnnouncementStatus status;
    private LocalDateTime createdAt;
    private String gender;
    private String health;
    private String personality;
    private int age;
    private Long shelterId;
    private LocalDate endDate;
    private AnnouncementStatus announcementStatus;
    private String animalType;

    public static AnnouncementListResponse from(Announcement announcement, PetResponse pet) {
        return AnnouncementListResponse.builder()
                                       .id(announcement.getId())
                                       .breed(pet.getBreed())
                                       .imageUrl(pet.getImageUrl())
                                       .status(announcement.getStatus())
                                       .createdAt(announcement.getCreatedAt())
                                       .gender(pet.getGender())
                                       .health(pet.getHealth())
                                       .personality(pet.getPersonality())
                                       .age(pet.getAge())
                                       .shelterId(announcement.getShelterId())
                                       .endDate(announcement.getEndDate())
                                       .announcementStatus(announcement.getStatus())
                                       .animalType(pet.getAnimalType())
                                       .build();
    }
}
