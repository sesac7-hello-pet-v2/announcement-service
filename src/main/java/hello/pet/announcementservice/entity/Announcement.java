package hello.pet.announcementservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
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

    private LocalDateTime endDate;
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

    public void updateEndDate(LocalDateTime newEndDate) {
        this.endDate = newEndDate;
        this.updatedAt = LocalDateTime.now();
    }

    public void softDelete() {
        this.status = AnnouncementStatus.DELETED;
        this.deletedAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void restore() {
        this.status = AnnouncementStatus.OPEN; // 복구 시 OPEN 상태로
        this.deletedAt = null;
        this.updatedAt = LocalDateTime.now();
    }
}
