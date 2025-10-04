package hello.pet.announcementservice.entity;

public enum AnnouncementStatus {
    OPEN,           // 등록됨 (활성 상태)
    IN_PROGRESS,    // 입양 절차 진행 중
    COMPLETED,      // 입양 완료
    CLOSED          // 종료됨
}
