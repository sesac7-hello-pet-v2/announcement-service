package hello.pet.announcementservice.repository;

import hello.pet.announcementservice.dto.response.AnnouncementListResponse;
import hello.pet.announcementservice.entity.Announcement;
import hello.pet.announcementservice.entity.AnnouncementStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {

    // IN_PROGRESS 상태인 공고만 조회
    List<Announcement> findByStatus(AnnouncementStatus status);

    // 특정 동물 ID 기준으로 조회 (petId로 명확히 이름 변경)
    Optional<Announcement> findByPetId(Long petId);

    // shelterId 기준 조회
    List<Announcement> findByShelterId(Long shelterId);

    // 공고 목록 조회 (Shelter 정보는 API로 가져와야 하므로 여기서는 제외)
    @Query("""
            SELECT NEW hello.pet.announcementservice.dto.response.AnnouncementListResponse(
                p.breed,
                p.imageUrl,
                a.status,
                a.id,
                a.createdAt
            )
            FROM Announcement a
            JOIN a.pet p
            WHERE a.status = :s
            """)
    Page<AnnouncementListResponse> searchAnnouncements(@Param("s") AnnouncementStatus s, Pageable pageable);

    // 내 공고 조회 (shelterId를 기준으로 조회)
    @Query("""
            SELECT NEW hello.pet.announcementservice.dto.response.AnnouncementListResponse(
                p.breed,
                p.imageUrl,
                a.status,
                a.id,
                a.createdAt
            )
            FROM Announcement a
            JOIN a.pet p
            WHERE a.shelterId = :shelterId
            """)
    Page<AnnouncementListResponse> searchMyAnnouncements(@Param("shelterId") Long shelterId, Pageable pageable);
}
