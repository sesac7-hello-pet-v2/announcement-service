package hello.pet.announcementservice.dto.response;

import hello.pet.announcementservice.entity.AnimalType;
import hello.pet.announcementservice.entity.AnnouncementStatus;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AnnouncementDetailResponse {
    private String id;
    private String breed;
    private String gender;
    private String health;
    private String personality;
    private int age;
    private String shelterName;
    private LocalDateTime createdAt;
    private LocalDateTime announcementPeriod;
    private String imageUrl;
    private AnnouncementStatus announcementStatus;
    private AnimalType animalType;
    private boolean alreadyApplied;
}
