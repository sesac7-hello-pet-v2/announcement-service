package hello.pet.announcementservice.repository;

import hello.pet.announcementservice.entity.Announcement;
import hello.pet.announcementservice.entity.AnnouncementStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {

    // 상태 기준 조회
    List<Announcement> findAllByStatus(AnnouncementStatus status);

    // 특정 동물 ID 기준 조회 (동일 petId에 여러 공고가 없다는 전제라면 Optional 유지)
    Optional<Announcement> findByPetId(Long petId);

    // shelterId 기준 조회 (대량 조회 예상 시 Pageable 사용 고려)
    List<Announcement> findAllByShelterId(Long shelterId);

    // 상태별 공고 페이징 조회
    Page<Announcement> findAllByStatus(AnnouncementStatus status, Pageable pageable);

    // 내 공고 페이징 조회
    Page<Announcement> findAllByShelterId(Long shelterId, Pageable pageable);
}
