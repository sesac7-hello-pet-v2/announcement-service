package hello.pet.announcementservice.dto.response;

/** 이번 완료 요청에서 실제로 상태를 변경했는지 반환한다. */
public record AnnouncementCompletionResponse(boolean changed) {
}
