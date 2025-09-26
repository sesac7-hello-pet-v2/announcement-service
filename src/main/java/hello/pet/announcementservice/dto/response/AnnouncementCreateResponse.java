package hello.pet.announcementservice.dto.response;

import hello.pet.announcementservice.entity.Announcement;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AnnouncementCreateResponse {
    private Long id;
    private String announcementId;
    private String shelterName;
    private String message;

    public static AnnouncementCreateResponse from(Announcement announcement, String shelterName) {
        return new AnnouncementCreateResponse(
                announcement.getId(),
                announcement.getShelterId() + "-" + announcement.getCreatedAt() + "-" + announcement.getId(),
                shelterName,
                "입양 공고 등록이 완료 되었습니다."
        );
    }
}
