package hello.pet.announcementservice.dto.request;

import hello.pet.announcementservice.entity.AnnouncementStatus;
import lombok.Getter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Getter
public class AnnouncementSearchRequest {

    private int page = 0; // 페이지 번호 (0부터 시작)

    private int size = 9; // 한 페이지에 표시할 데이터 개수

    private AnnouncementStatus status = AnnouncementStatus.OPEN;

    public Pageable toPageable() {
        return PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
    }
}
