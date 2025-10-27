package hello.pet.announcementservice.repository;

import hello.pet.announcementservice.entity.Announcement;
import hello.pet.announcementservice.entity.AnnouncementStatus;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {
    Optional<Announcement> findByIdAndStatusNot(Long id, AnnouncementStatus status);

    Page<Announcement> findAllByStatusNot(AnnouncementStatus status, Pageable pageable);

    Page<Announcement> findAllByStatus(AnnouncementStatus status, Pageable pageable);
}
