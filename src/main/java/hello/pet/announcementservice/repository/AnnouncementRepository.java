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

    /**
     * 상태 기준 전체 조회 (비페이징)
     */
    List<Announcement> findAllByStatus(AnnouncementStatus status);

    /**
     * 상태별 공고 페이징 조회
     */
    Page<Announcement> findAllByStatus(AnnouncementStatus status, Pageable pageable);

    /**
     * 보호소별 공고 페이징 조회
     */
    Page<Announcement> findAllByShelterId(Long shelterId, Pageable pageable);

    /**
     * 특정 펫 ID로 공고 조회
     * (펫 1마리당 공고 1건이라는 전제)
     */
    Optional<Announcement> findByPetId(Long petId);
}
