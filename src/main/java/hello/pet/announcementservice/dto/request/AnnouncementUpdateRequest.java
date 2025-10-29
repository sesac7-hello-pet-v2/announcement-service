package hello.pet.announcementservice.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import hello.pet.announcementservice.entity.AnnouncementStatus;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AnnouncementUpdateRequest {

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    private AnnouncementStatus status;
}
