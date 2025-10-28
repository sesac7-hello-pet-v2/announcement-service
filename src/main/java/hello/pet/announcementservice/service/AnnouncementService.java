package hello.pet.announcementservice.service;

import hello.pet.announcementservice.dto.request.AnnouncementCreateRequest;
import hello.pet.announcementservice.dto.request.AnnouncementSearchRequest;
import hello.pet.announcementservice.dto.request.AnnouncementUpdateRequest;
import hello.pet.announcementservice.dto.response.AnnouncementCreateResponse;
import hello.pet.announcementservice.dto.response.AnnouncementDetailResponse;
import hello.pet.announcementservice.dto.response.AnnouncementListResponse;
import hello.pet.announcementservice.dto.response.AnnouncementPageResponse;
import hello.pet.announcementservice.dto.response.AnnouncementUpdateResponse;
import hello.pet.announcementservice.dto.response.PetResponse;
import hello.pet.announcementservice.entity.Announcement;
import hello.pet.announcementservice.entity.AnnouncementStatus;
import hello.pet.announcementservice.exception.UnauthorizedOperationException;
import hello.pet.announcementservice.facade.ApplicationServiceFacade;
import hello.pet.announcementservice.facade.PetServiceFacade;
import hello.pet.announcementservice.facade.UserServiceFacade;
import hello.pet.announcementservice.repository.AnnouncementRepository;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.ZoneId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AnnouncementService {

    private final AnnouncementRepository announcementRepository;
    private final UserServiceFacade userServiceFacade;
    private final PetServiceFacade petServiceFacade;
    private final ApplicationServiceFacade applicationServiceFacade;

    public AnnouncementCreateResponse createAnnouncement(AnnouncementCreateRequest request, Long shelterId) {
        validatePet(request.getPetId(), shelterId);
        validateEndDate(request.getEndDate());

        Announcement announcement = saveAnnouncement(request, shelterId);

        updatePetAsAnnounced(request.getPetId(), shelterId);

        String shelterName = userServiceFacade.getNickname(shelterId).orElse("닉네임 없음");
        return AnnouncementCreateResponse.from(announcement, shelterName);
    }

    @Transactional(readOnly = true)
    public AnnouncementPageResponse getAllAnnouncements(AnnouncementSearchRequest request) {
        Page<Announcement> announcements;
        if (request.getStatus() != null) {
            // 특정 상태 조회
            announcements = announcementRepository.findAllByStatus(
                    request.getStatus(),
                    request.toPageable()
            );
        } else {
            // 전체 조회 시 DELETED만 제외
            announcements = announcementRepository.findAllByStatusNot(
                    AnnouncementStatus.DELETED,
                    request.toPageable()
            );
        }

        Page<AnnouncementListResponse> responses = announcements.map(announcement -> {
            PetResponse pet = petServiceFacade.getPet(announcement.getPetId());
            return AnnouncementListResponse.from(announcement, pet);
        });

        return AnnouncementPageResponse.from(responses, request);
    }

    @Transactional(readOnly = true)
    public AnnouncementPageResponse getMyAnnouncements(AnnouncementSearchRequest request, Long shelterId) {

        Page<Announcement> announcements = announcementRepository.findAllByShelterId(
                shelterId,
                request.toPageable()
        );

        Page<AnnouncementListResponse> responses = announcements.map(announcement -> {
            PetResponse pet = petServiceFacade.getPet(announcement.getPetId());
            return AnnouncementListResponse.from(announcement, pet);
        });

        return AnnouncementPageResponse.from(responses, request);
    }

    public Announcement findById(Long announcementId) {
        return announcementRepository.findByIdAndStatusNot(announcementId, AnnouncementStatus.DELETED)
                                     .orElseThrow(() -> new EntityNotFoundException(
                                             "입양 공고를 찾을 수 없습니다. id=" + announcementId));
    }

    @Transactional(readOnly = true)
    public AnnouncementDetailResponse getAnnouncementDetail(Long id, Long userIdOrNull) {
        Announcement announcement = findById(id);
        PetResponse pet = petServiceFacade.getPet(announcement.getPetId());

        // 로그인 사용자가 이미 신청한 공고인지 확인해, 프론트에서 신청 버튼 표시 여부를 결정하는 데 사용
        boolean alreadyApplied = (userIdOrNull != null) &&
                applicationServiceFacade.hasUserAppliedToAnnouncement(announcement.getId(), userIdOrNull);

        String shelterName = userServiceFacade.getNickname(announcement.getShelterId()).orElse("닉네임 없음");

        return AnnouncementDetailResponse.from(announcement, pet, shelterName, alreadyApplied);
    }

    public AnnouncementUpdateResponse updateAnnouncement(Long announcementId,
                                                         AnnouncementUpdateRequest request,
                                                         Long shelterId) {
        Announcement announcement = findById(announcementId);

        validateOwnership(announcement, shelterId);
        if (request.getEndDate() != null) {
            validateEndDate(request.getEndDate());
        }

        applyUpdates(announcement, request);

        PetResponse pet = petServiceFacade.getPet(announcement.getPetId());
        return AnnouncementUpdateResponse.from(announcement, pet);
    }

    public void deleteAnnouncement(Long announcementId, Long shelterId) {
        Announcement announcement = findById(announcementId);
        validateOwnership(announcement, shelterId);

        petServiceFacade.markAsAvailable(announcement.getPetId(), shelterId, "SHELTER");

        announcement.softDelete();
        announcementRepository.save(announcement);
    }

    public void completeAnnouncement(Long id, Long shelterId) {
        Announcement announcement = findById(id);
        validateOwnership(announcement, shelterId);
        announcement.changeStatus(AnnouncementStatus.COMPLETED);
        announcement.updateTimestamp();
    }

    private void validatePet(Long petId, Long shelterId) {
        PetResponse pet = petServiceFacade.getPet(petId);
        if (pet == null) {
            throw new EntityNotFoundException("Pet을 찾을 수 없습니다. petId=" + petId);
        }

        // 펫을 등록한 보호소만 공고를 생성할 수 있도록 검증
        if (!pet.getShelterId().equals(shelterId)) {
            throw new UnauthorizedOperationException("해당 펫에 대한 공고를 생성할 권한이 없습니다.");
        }

        if ("ANNOUNCED".equals(pet.getStatus())) {
            throw new IllegalStateException("이미 공고 등록된 펫입니다.");
        }
        if ("ADOPTED".equals(pet.getStatus())) {
            throw new IllegalStateException("이미 입양된 펫은 공고할 수 없습니다.");
        }
    }

    private void validateEndDate(LocalDate endDate) {
        if (endDate == null) {
            throw new IllegalArgumentException("공고 종료일은 필수입니다.");
        }
        LocalDate today = LocalDate.now(ZoneId.of("Asia/Seoul"));
        if (endDate.isBefore(today)) {
            throw new IllegalArgumentException("공고 종료일은 오늘 이후 날짜여야 합니다.");
        }
    }

    private Announcement saveAnnouncement(AnnouncementCreateRequest request, Long shelterId) {
        Announcement announcement = request.toEntity(shelterId);
        return announcementRepository.save(announcement);
    }

    private void updatePetAsAnnounced(Long petId, Long shelterId) {
        petServiceFacade.markAsAnnounced(petId, shelterId, "SHELTER");
    }

    private void validateOwnership(Announcement announcement, Long shelterId) {
        if (!announcement.getShelterId().equals(shelterId)) {
            throw new UnauthorizedOperationException("해당 공고를 수정할 권한이 없습니다.");
        }
    }

    private void applyUpdates(Announcement announcement, AnnouncementUpdateRequest request) {
        if (request.getEndDate() != null) {
            announcement.updateEndDate(request.getEndDate());
        }
        if (request.getStatus() != null) {
            announcement.changeStatus(request.getStatus());
        }
        announcement.updateTimestamp();
    }

    /**
     * 공고 마감 시 해당 공고의 신청들을 UNDER_REVIEW 상태로 변경
     * 스케줄러에서 호출됨
     */
    public void updateApplicationStatusToUnderReview(Long announcementId) {
        try {
            log.info("공고 ID {}의 신청 상태 업데이트 시작", announcementId);
            applicationServiceFacade.updateApplicationsToUnderReviewForClosedAnnouncement(announcementId);
            log.info("공고 ID {}의 신청 상태 업데이트 완료", announcementId);
        } catch (Exception e) {
            log.error("공고 ID {}의 신청 상태 업데이트 중 오류 발생", announcementId, e);
            // 실패해도 공고 마감 처리는 계속 진행
        }
    }
}
