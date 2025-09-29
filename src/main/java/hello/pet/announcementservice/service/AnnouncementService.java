package hello.pet.announcementservice.service;

import hello.pet.announcementservice.dto.request.AnnouncementCreateRequest;
import hello.pet.announcementservice.dto.request.AnnouncementSearchRequest;
import hello.pet.announcementservice.dto.request.AnnouncementUpdateRequest;
import hello.pet.announcementservice.dto.response.AnnouncementCreateResponse;
import hello.pet.announcementservice.dto.response.AnnouncementDetailResponse;
import hello.pet.announcementservice.dto.response.AnnouncementListResponse;
import hello.pet.announcementservice.dto.response.AnnouncementPageResponse;
import hello.pet.announcementservice.dto.response.AnnouncementUpdateResponse;
import hello.pet.announcementservice.entity.Announcement;
import hello.pet.announcementservice.entity.AnimalType;
import hello.pet.announcementservice.entity.AnnouncementStatus;
import hello.pet.announcementservice.entity.Pet;
import hello.pet.announcementservice.repository.AnnouncementRepository;
import hello.pet.announcementservice.repository.PetRepository;
import hello.pet.announcementservice.service.facade.ApplicationServiceFacade;
import hello.pet.announcementservice.service.facade.UserServiceFacade;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AnnouncementService {

    private final PetRepository petRepository;
    private final AnnouncementRepository announcementRepository;
    private final UserServiceFacade userServiceFacade;
    private final ApplicationServiceFacade applicationServiceFacade;

    /**
     * 게시글 등록
     */
    public AnnouncementCreateResponse createAnnouncement(
            AnnouncementCreateRequest request,
            Long shelterId   // JWT 등에서 추출해 컨트롤러에서 넘겨줌
    ) {
        Pet pet = Pet.builder()
                     .animalType(AnimalType.valueOf(request.getAnimalType().toUpperCase()))
                     .breed(request.getBreed())
                     .gender(request.getGender())
                     .age(request.getAge())
                     .health(request.getHealth())
                     .personality(request.getPersonality())
                     .imageUrl(request.getImage())
                     .build();
        petRepository.save(pet);

        Announcement announcement = Announcement.builder()
                                                .shelterId(shelterId)
                                                .pet(pet)
                                                .status(AnnouncementStatus.IN_PROGRESS)
                                                .announcementPeriod(request.getSelectedDate())
                                                .createdAt(LocalDateTime.now())
                                                .build();

        announcementRepository.save(announcement);

        String shelterName = userServiceFacade.getShelterName(shelterId);
        return AnnouncementCreateResponse.from(announcement, shelterName);
    }

    /**
     * 게시글 전체 리스트 조회
     */
    @Transactional(readOnly = true)
    public AnnouncementPageResponse getAllAnnouncements(AnnouncementSearchRequest request) {
        Page<AnnouncementListResponse> announcements =
                announcementRepository.searchAnnouncements(
                        AnnouncementStatus.IN_PROGRESS, request.toPageable());
        return AnnouncementPageResponse.from(announcements, request);
    }

    /**
     * 단건 조회
     */
    public Announcement findById(Long announcementId) {
        return announcementRepository.findById(announcementId)
                                     .orElseThrow(() -> new EntityNotFoundException(
                                             "입양 공고를 찾을 수 없습니다. id=" + announcementId));
    }

    /**
     * 상세 조회
     */
    @Transactional(readOnly = true)
    public AnnouncementDetailResponse getAnnouncementDetail(Long id, Long userIdOrNull) {
        Announcement announcement = findById(id);
        Pet pet = announcement.getPet();

        boolean alreadyApplied = false;
        if (userIdOrNull != null) {
            alreadyApplied = applicationServiceFacade
                    .existsByAnnouncementIdAndApplicantId(announcement.getId(), userIdOrNull);
        }

        String shelterName = userServiceFacade.getShelterName(announcement.getShelterId());

        return AnnouncementDetailResponse.builder()
                                         .id(String.valueOf(announcement.getId()))
                                         .breed(pet.getBreed())
                                         .gender(pet.getGender())
                                         .health(pet.getHealth())
                                         .personality(pet.getPersonality())
                                         .shelterName(shelterName)
                                         .createdAt(announcement.getCreatedAt())
                                         .announcementPeriod(announcement.getAnnouncementPeriod())
                                         .imageUrl(pet.getImageUrl())
                                         .announcementStatus(announcement.getStatus())
                                         .animalType(pet.getAnimalType())
                                         .alreadyApplied(alreadyApplied)
                                         .build();
    }

    /**
     * 수정
     */
    public AnnouncementUpdateResponse updateAnnouncement(Long announcementId, AnnouncementUpdateRequest request) {
        Announcement announcement = findById(announcementId);

        Pet pet = announcement.getPet();
        pet.updateInfo(
                request.getBreed(),
                request.getGender(),
                request.getAge(),
                request.getHealth(),
                request.getPersonality(),
                request.getImage(),
                AnimalType.valueOf(request.getAnimalType().toUpperCase())
        );

        announcement.updateTimestamp();
        return AnnouncementUpdateResponse.from(announcement);
    }

    /**
     * 삭제
     */
    public void deleteAnnouncement(Long announcementId) {
        Announcement announcement = findById(announcementId);
        announcementRepository.delete(announcement);
    }

    /**
     * 내가 쓴 공고 조회
     */
    @Transactional(readOnly = true)
    public AnnouncementPageResponse getMyAnnouncements(Long shelterId, Pageable pageable) {
        Page<AnnouncementListResponse> announcementListResponses =
                announcementRepository.searchMyAnnouncements(shelterId, pageable);
        return AnnouncementPageResponse.from(announcementListResponses, new AnnouncementSearchRequest());
    }

    /**
     * 상태 완료 처리
     */
    public void completeAnnouncement(Long id) {
        Announcement announcement = findById(id);
        announcement.changeStatus(AnnouncementStatus.COMPLETED);
    }
}
