package hello.pet.announcementservice.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import hello.pet.announcementservice.entity.Announcement;
import hello.pet.announcementservice.entity.AnnouncementStatus;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AnnouncementCreateRequest {

    @NotNull
    private Long petId;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime endDate;

    public Announcement toEntity(Long shelterId) {
        return Announcement.builder()
                           .shelterId(shelterId)
                           .petId(this.petId)
                           .status(AnnouncementStatus.IN_PROGRESS)
                           .endDate(this.endDate)
                           .createdAt(LocalDateTime.now())
                           .build();
    }
}

