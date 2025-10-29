package hello.pet.announcementservice.repository;

import hello.pet.announcementservice.entity.Announcement;
import hello.pet.announcementservice.entity.AnnouncementStatus;
import java.time.LocalDate;
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
    Optional<Announcement> findByIdAndStatusNot(Long id, AnnouncementStatus status);

    Page<Announcement> findAllByStatusNot(AnnouncementStatus status, Pageable pageable);

    Page<Announcement> findAllByStatus(AnnouncementStatus status, Pageable pageable);

    Page<Announcement> findAllByShelterId(Long shelterId, Pageable pageable);

    // 특정 날짜가 마감일인 공고 조회 (스케줄러에서 어제 날짜로 조회하여 오늘 마감 처리)
    @Query("SELECT a FROM Announcement a WHERE a.endDate = :date AND a.status = :status")
    List<Announcement> findByEndDateAndStatus(@Param("date") LocalDate date,
                                              @Param("status") AnnouncementStatus status);

    // 마감일이 특정 날짜 이전인 미처리 공고 조회 (서버 장애 등으로 마감 처리를 놓친 공고 처리용)
    @Query("SELECT a FROM Announcement a WHERE a.endDate < :date AND a.status = :status")
    List<Announcement> findExpiredAnnouncements(@Param("date") LocalDate date,
                                                @Param("status") AnnouncementStatus status);

    // 특정 펫으로 등록된 활성 공고가 있는지 확인 (펫 삭제 방지용)
    boolean existsByPetIdAndStatusNot(Long petId, AnnouncementStatus status);
}
