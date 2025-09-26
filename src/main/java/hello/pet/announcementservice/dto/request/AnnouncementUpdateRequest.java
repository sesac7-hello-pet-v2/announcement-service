package hello.pet.announcementservice.dto.request;

import lombok.Data;

@Data
public class AnnouncementUpdateRequest {
    private String breed;              // 견종
    private String gender;             // 성별
    private String health;             // 건강상태
    private String personality;        // 성격
    private int age;                   // 나이
    private int Date;                  // 공고 기간
    private String image;              // 이미지
    private String animalType;         // 동물 종류
}
