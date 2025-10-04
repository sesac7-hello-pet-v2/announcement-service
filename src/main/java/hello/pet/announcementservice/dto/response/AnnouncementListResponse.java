package hello.pet.announcementservice.dto.response;

import hello.pet.announcementservice.entity.Announcement;
import hello.pet.announcementservice.entity.AnnouncementStatus;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public class AnnouncementListResponse {
    private String breed;
    private String imageUrl;
    private AnnouncementStatus status;
    private Long id;
    private LocalDateTime createdAt;

    public static AnnouncementListResponse from(Announcement announcement, PetResponse pet) {
        return AnnouncementListResponse.builder()
                                       .breed(pet.getBreed())
                                       .imageUrl(pet.getImageUrl())
                                       .status(announcement.getStatus())
                                       .id(announcement.getId())
                                       .createdAt(announcement.getCreatedAt())
                                       .build();
    }
}
