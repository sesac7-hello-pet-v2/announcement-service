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
import org.springframework.data.domain.Pageable;
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
@RequestMapping("/v1/announcements")
@RequiredArgsConstructor
public class AnnouncementController {

    private final AnnouncementService announcementService;

    @PostMapping
    public ResponseEntity<AnnouncementCreateResponse> createAnnouncement(@RequestBody AnnouncementCreateRequest request,
                                                                         @RequestHeader("X-User-Id") Long shelterId) {
        AnnouncementCreateResponse response = announcementService.createAnnouncement(request, shelterId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<AnnouncementPageResponse> getAllAnnouncements(
            @ModelAttribute AnnouncementSearchRequest request) {
        return ResponseEntity.ok(announcementService.getAllAnnouncements(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnnouncementDetailResponse> getAnnouncementDetail(
            @PathVariable Long id,
            @RequestHeader(value = "X-User-Id", required = false) Long userId
    ) {
        return ResponseEntity.ok(announcementService.getAnnouncementDetail(id, userId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AnnouncementUpdateResponse> updateAnnouncement(@PathVariable Long id,
                                                                         @RequestBody AnnouncementUpdateRequest request,
                                                                         @RequestHeader("X-User-Id") Long shelterId) {
        AnnouncementUpdateResponse updated = announcementService.updateAnnouncement(id, request, shelterId);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAnnouncement(@PathVariable Long id,
                                                   @RequestHeader("X-User-Id") Long shelterId) {
        announcementService.deleteAnnouncement(id, shelterId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    public ResponseEntity<AnnouncementPageResponse> getMyAnnouncements(@RequestHeader("X-User-Id") Long shelterId,
                                                                       Pageable pageable) {
        return ResponseEntity.ok(announcementService.getMyAnnouncements(shelterId, pageable));
    }
}
