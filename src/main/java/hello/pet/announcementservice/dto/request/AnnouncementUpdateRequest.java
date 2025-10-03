package hello.pet.announcementservice.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import hello.pet.announcementservice.entity.AnnouncementStatus;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnnouncementUpdateRequest {
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime announcementPeriod;      // 공고 기간
    private AnnouncementStatus status;             // 공고 상태
}
