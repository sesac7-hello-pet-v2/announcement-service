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
    @Enumerated(EnumType.STRING)
    private AnnouncementStatus status;

    @Column(nullable = false)
    private Long petId;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime announcementPeriod;

    public void updateTimestamp() {
        this.updatedAt = LocalDateTime.now();
    }

    public void changeStatus(AnnouncementStatus newStatus) {
        this.status = newStatus;
        this.updatedAt = LocalDateTime.now();
    }

    public void updateAnnouncementPeriod(LocalDateTime newPeriod) {
        this.announcementPeriod = newPeriod;
        this.updatedAt = LocalDateTime.now();
    }
}
