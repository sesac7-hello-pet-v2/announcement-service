package hello.pet.announcementservice.exception;

public class AnnouncementCompletionConflictException extends IllegalStateException {
    public AnnouncementCompletionConflictException() {
        super("공고 상태가 변경되어 완료를 취소할 수 없습니다.");
    }
}
