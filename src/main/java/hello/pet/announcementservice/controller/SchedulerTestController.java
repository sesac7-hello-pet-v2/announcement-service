package hello.pet.announcementservice.controller;

import hello.pet.announcementservice.scheduler.AnnouncementScheduler;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 스케줄러를 수동으로 실행하기 위한 테스트 컨트롤러
 * <p>
 * ⚠️ 주의: 개발/테스트 환경 전용 - 운영 환경에서 절대 활성화 금지
 * <p>
 * 활성화 방법:
 * IntelliJ: Run Configuration > Environment variables에 추가 > ENABLE_SCHEDULER_TEST=true
 */
@Slf4j
@RestController
@RequestMapping("/v1/admin/scheduler")
@RequiredArgsConstructor
@ConditionalOnProperty(
        name = "ENABLE_SCHEDULER_TEST",
        havingValue = "true",
        matchIfMissing = false  // 기본값 false (안전을 위해 명시)
)
public class SchedulerTestController {

    private final AnnouncementScheduler announcementScheduler;

    @PostMapping("/trigger/close-announcements")
    public ResponseEntity<Map<String, Object>> triggerCloseAnnouncements() {
        log.info("=== [수동 실행] 공고 마감 스케줄러 시작 ===");

        Map<String, Object> response = new HashMap<>();
        response.put("startTime", LocalDateTime.now());

        try {
            // 스케줄러 실행
            announcementScheduler.closeExpiredAnnouncements();

            response.put("status", "SUCCESS");
            response.put("message", "공고 마감 스케줄러가 성공적으로 실행되었습니다.");
            response.put("endTime", LocalDateTime.now());

            log.info("=== [수동 실행] 공고 마감 스케줄러 완료 ===");

        } catch (Exception e) {
            response.put("status", "FAILURE");
            response.put("message", "스케줄러 실행 중 오류 발생: " + e.getMessage());
            response.put("error", e.getClass().getSimpleName());
            response.put("endTime", LocalDateTime.now());

            log.error("=== [수동 실행] 공고 마감 스케줄러 실행 실패 ===", e);

            return ResponseEntity.internalServerError().body(response);
        }

        return ResponseEntity.ok(response);
    }
}
