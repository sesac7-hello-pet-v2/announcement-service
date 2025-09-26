package hello.pet.announcementservice.dto.request;

import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Data
public class AnnouncementSearchRequest {

    private Integer page = 0;
    private Integer size = 9;
    private AnnouncementSortType requestSortType = AnnouncementSortType.CREATEDAT;
    private AnnouncementAscDescType requestAscDescType = AnnouncementAscDescType.DESC;

    public Pageable toPageable() {
        Sort.Direction direction =
                (requestAscDescType == AnnouncementAscDescType.DESC)
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        String sortProperty = switch (requestSortType) {
            case SHELTER -> "shelter";
            case ID -> "id";
            case CREATEDAT -> "createdAt";
        };

        return PageRequest.of(page, size, Sort.by(direction, sortProperty));
    }
}
