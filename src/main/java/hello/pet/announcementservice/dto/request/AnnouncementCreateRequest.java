package hello.pet.announcementservice.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AnnouncementCreateRequest {
    private String breed;                           // 견종
    private String animalType;                      // 동물 종류
    private String gender;                          // 성별
    private String health;                          // 건강상태
    private String personality;                     // 성격
    private int age;                                // 나이
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime selectedDate;
    private String image;                           // 이미지 URL 또는 Base64
}

