package hello.pet.announcementservice.dto.request;

import hello.pet.announcementservice.entity.AnnouncementStatus;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class AnnouncementUpdateRequest {
    private LocalDateTime announcementPeriod;      // 공고 기간
    private AnnouncementStatus status;             // 공고 상태
}
