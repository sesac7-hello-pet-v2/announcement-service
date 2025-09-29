package hello.pet.announcementservice.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AnnouncementCreateRequest {
    private Long petId;                             // Pet Service에서 생성된 Pet ID
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime selectedDate;
}

