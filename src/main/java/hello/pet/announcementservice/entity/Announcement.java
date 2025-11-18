package hello.pet.announcementservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@Entity
@Table(name = "announcements")
@NoArgsConstructor
@AllArgsConstructor
public class Announcement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long shelterId;

    @Column(nullable = false)
    private Long petId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AnnouncementStatus status;

    @Column(name = "end_date")
    private LocalDate endDate;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    public void updateTimestamp() {
        this.updatedAt = LocalDateTime.now();
    }

    public void changeStatus(AnnouncementStatus newStatus) {
        this.status = newStatus;
        this.updatedAt = LocalDateTime.now();
    }

    public void updateEndDate(LocalDate newEndDate) {
        this.endDate = newEndDate;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * 공고 마감일 경과 여부 확인
     *
     * @return 마감일이 지났으면 true, 아니면 false
     */
    public boolean isExpired() {
        if (this.endDate == null) {
            return false;
        }
        return LocalDate.now(ZoneId.of("Asia/Seoul")).isAfter(this.endDate);
    }

    public void softDelete() {
        this.status = AnnouncementStatus.DELETED;
        this.deletedAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void reopen() {
        if (this.status == AnnouncementStatus.COMPLETED) {
            this.status = AnnouncementStatus.OPEN;
            this.updatedAt = LocalDateTime.now();
        }
    }
}
