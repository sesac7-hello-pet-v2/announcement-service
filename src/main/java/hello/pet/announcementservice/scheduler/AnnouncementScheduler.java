package hello.pet.announcementservice.scheduler;

import hello.pet.announcementservice.entity.Announcement;
import hello.pet.announcementservice.entity.AnnouncementStatus;
import hello.pet.announcementservice.repository.AnnouncementRepository;
import hello.pet.announcementservice.service.AnnouncementService;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class AnnouncementScheduler {

    private final AnnouncementRepository announcementRepository;
    private final AnnouncementService announcementService;

    /**
     * 매일 자정에 실행되어 마감일이 지난 공고들을 자동으로 마감 처리
     * cron = "0 0 0 * * *" : 초 분 시 일 월 요일
     * zone = "Asia/Seoul" : 한국 시간 기준
     */
    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")
    @Transactional
    public void closeExpiredAnnouncements() {
        log.info("=== 공고 자동 마감 스케줄러 시작 ===");

        try {
            LocalDate today = LocalDate.now(ZoneId.of("Asia/Seoul"));

            // 1. 오늘 마감되는 공고들 조회 (endDate가 어제인 공고들)
            // 예: 1월 15일이 endDate면, 1월 16일 00:00에 마감 처리
            LocalDate yesterday = today.minusDays(1);
            List<Announcement> todayClosingAnnouncements =
                    announcementRepository.findByEndDateAndStatus(yesterday, AnnouncementStatus.OPEN);

            if (!todayClosingAnnouncements.isEmpty()) {
                log.info("오늘 마감 처리할 공고 수: {}", todayClosingAnnouncements.size());
                for (Announcement announcement : todayClosingAnnouncements) {
                    processAnnouncementClosure(announcement);
                }
            }

            // 2. 이미 마감일이 지난 공고들도 처리 (서버 다운타임 등으로 놓친 경우 대비)
            List<Announcement> expiredAnnouncements =
                    announcementRepository.findExpiredAnnouncements(yesterday, AnnouncementStatus.OPEN);

            if (!expiredAnnouncements.isEmpty()) {
                log.warn("마감일이 지났지만 처리되지 않은 공고 수: {}", expiredAnnouncements.size());
                for (Announcement announcement : expiredAnnouncements) {
                    processAnnouncementClosure(announcement);
                }
            }

            log.info("=== 공고 자동 마감 스케줄러 완료 ===");

        } catch (Exception e) {
            log.error("공고 자동 마감 처리 중 오류 발생", e);
        }
    }

    /**
     * 개별 공고 마감 처리
     */
    private void processAnnouncementClosure(Announcement announcement) {
        try {
            log.info("공고 ID {} 마감 처리 시작", announcement.getId());

            // 공고 상태를 CLOSED로 변경
            announcement.changeStatus(AnnouncementStatus.CLOSED);
            announcementRepository.save(announcement);

            // OpenFeign을 통해 application-service 호출
            announcementService.updateApplicationStatusToUnderReview(announcement.getId());

            log.info("공고 ID {} 마감 처리 완료", announcement.getId());

        } catch (Exception e) {
            log.error("공고 ID {} 마감 처리 중 오류 발생", announcement.getId(), e);
        }
    }
}
