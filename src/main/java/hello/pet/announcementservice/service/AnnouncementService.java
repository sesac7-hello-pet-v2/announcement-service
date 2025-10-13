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
import hello.pet.announcementservice.repository.AnnouncementRepository;
import hello.pet.announcementservice.facade.ApplicationServiceFacade;
import hello.pet.announcementservice.facade.PetServiceFacade;
import hello.pet.announcementservice.facade.UserServiceFacade;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AnnouncementService {

    private final AnnouncementRepository announcementRepository;
    private final UserServiceFacade userServiceFacade;
    private final PetServiceFacade petServiceFacade;
    private final ApplicationServiceFacade applicationServiceFacade;

    public AnnouncementCreateResponse createAnnouncement(AnnouncementCreateRequest request, Long shelterId) {
        validatePet(request.getPetId());
        validateEndDate(request.getEndDate());

        Announcement announcement = saveAnnouncement(request, shelterId);

        updatePetAsAnnounced(request.getPetId());

        String shelterName = userServiceFacade.getShelterName(shelterId);
        return AnnouncementCreateResponse.from(announcement, shelterName);
    }

    @Transactional(readOnly = true)
    public AnnouncementPageResponse getAllAnnouncements(AnnouncementSearchRequest request) {
        Page<Announcement> announcements = announcementRepository.findAllByStatus(
                request.getStatus(),
                request.toPageable()
        );

        Page<AnnouncementListResponse> responses = announcements.map(a -> {
            PetResponse pet = petServiceFacade.getPet(a.getPetId());
            return AnnouncementListResponse.from(a, pet);
        });

        return AnnouncementPageResponse.from(responses, request);
    }

    public Announcement findById(Long announcementId) {
        return announcementRepository.findById(announcementId)
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

        String shelterName = userServiceFacade.getShelterName(announcement.getShelterId());

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
        announcementRepository.delete(announcement);
    }

    @Transactional
    public void completeAnnouncement(Long id, Long shelterId) {
        Announcement announcement = findById(id);
        validateOwnership(announcement, shelterId);
        announcement.changeStatus(AnnouncementStatus.COMPLETED);
        announcement.updateTimestamp();
    }

    private void validatePet(Long petId) {
        PetResponse pet = petServiceFacade.getPet(petId);
        if (pet == null) {
            throw new EntityNotFoundException("Pet을 찾을 수 없습니다. petId=" + petId);
        }
        if (Boolean.TRUE.equals(pet.getAnnounced())) {
            throw new IllegalStateException("이미 공고 등록된 펫입니다.");
        }
    }

    private void validateEndDate(LocalDateTime endDate) {
        if (endDate == null) {
            throw new IllegalArgumentException("공고 종료일은 필수입니다.");
        }
        if (endDate.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("공고 종료일은 현재 시간 이후여야 합니다.");
        }
    }

    private Announcement saveAnnouncement(AnnouncementCreateRequest request, Long shelterId) {
        Announcement announcement = request.toEntity(shelterId);
        return announcementRepository.save(announcement);
    }

    private void updatePetAsAnnounced(Long petId) {
        try {
            petServiceFacade.markAsAnnounced(petId);
        } catch (Exception e) {
            throw new IllegalStateException("Pet 상태 업데이트 실패", e);
        }
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
}
