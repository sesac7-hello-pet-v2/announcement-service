package hello.pet.announcementservice.controller;

import hello.pet.announcementservice.dto.request.AnnouncementCreateRequest;
import hello.pet.announcementservice.dto.request.AnnouncementSearchRequest;
import hello.pet.announcementservice.dto.request.AnnouncementUpdateRequest;
import hello.pet.announcementservice.dto.response.AnnouncementCreateResponse;
import hello.pet.announcementservice.dto.response.AnnouncementDetailResponse;
import hello.pet.announcementservice.dto.response.AnnouncementPageResponse;
import hello.pet.announcementservice.dto.response.AnnouncementUpdateResponse;
import hello.pet.announcementservice.service.AnnouncementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/announcements")
@RequiredArgsConstructor
public class AnnouncementController {

    private final AnnouncementService announcementService;

    /**
     * 게시글 등록
     */
    @PostMapping
    public ResponseEntity<AnnouncementCreateResponse> createAnnouncement(
            @RequestBody AnnouncementCreateRequest request,
            @RequestHeader("X-User-Id") Long shelterId
    ) {
        AnnouncementCreateResponse response = announcementService.createAnnouncement(request, shelterId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * 전체 입양 게시글 리스트 조회
     */
    @GetMapping
    public ResponseEntity<AnnouncementPageResponse> getAllAnnouncements(
            @ModelAttribute AnnouncementSearchRequest request) {
        return ResponseEntity.ok(announcementService.getAllAnnouncements(request));
    }

    /**
     * 입양 공고 상세 조회
     */
    @GetMapping("/{id}")
    public ResponseEntity<AnnouncementDetailResponse> getAnnouncementDetail(
            @PathVariable Long id,
            @RequestHeader(value = "X-User-Id", required = false) Long userId
    ) {
        AnnouncementDetailResponse detail = announcementService.getAnnouncementDetail(id, userId);
        return ResponseEntity.ok(detail);
    }

    /**
     * 입양 공고 게시글 수정
     */
    @PutMapping("/{id}")
    public ResponseEntity<AnnouncementUpdateResponse> updateAnnouncement(
            @PathVariable Long id,
            @RequestBody AnnouncementUpdateRequest request,
            @RequestHeader("X-User-Id") Long shelterId
    ) {
        AnnouncementUpdateResponse updated = announcementService.updateAnnouncement(id, request);
        return ResponseEntity.ok(updated);
    }

    /**
     * 입양 공고 게시글 삭제
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAnnouncement(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") Long shelterId
    ) {
        announcementService.deleteAnnouncement(id);
        return ResponseEntity.ok("삭제가 완료되었습니다.");
    }
}
