package hello.pet.announcementservice.entity;

public enum AnnouncementStatus {
    OPEN,           // 공고 중 (기본 등록 상태)
    IN_PROGRESS,    // 입양 절차 진행 중
    COMPLETED,      // 입양 완료
    CLOSED          // 기간 만료 또는 종료
}
