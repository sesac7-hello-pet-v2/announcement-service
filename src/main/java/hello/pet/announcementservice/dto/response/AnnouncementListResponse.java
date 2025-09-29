package hello.pet.announcementservice.dto.response;

import hello.pet.announcementservice.entity.AnnouncementStatus;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
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
