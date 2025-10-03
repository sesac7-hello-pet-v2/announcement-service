package hello.pet.announcementservice.dto.response;

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
public class AnnouncementListResponse {
    private String breed;
    private String image;
    private AnnouncementStatus status;
    private Long id;
    private LocalDateTime createdAt;
}
