package hello.pet.announcementservice;

import hello.pet.announcementservice.entity.Announcement;
import hello.pet.announcementservice.entity.AnnouncementStatus;
import hello.pet.announcementservice.repository.AnnouncementRepository;
import hello.pet.announcementservice.service.AnnouncementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static hello.pet.announcementservice.entity.AnnouncementStatus.*;
import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/** 서비스 호출마다 실제 DB 트랜잭션을 커밋하며 HTTP 응답 계약도 검증한다. */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AnnouncementCompletionTest {
    @Autowired AnnouncementService service;
    @Autowired AnnouncementRepository repository;
    @Autowired MockMvc mvc;

    @BeforeEach
    void clean() {
        repository.deleteAll();
    }

    @Test
    void completionAndCompensationRestoreClosedState() throws Exception {
        Long id = save(CLOSED);
        mvc.perform(patch("/v1/announcements/{id}/complete", id).header("X-User-Id", 10L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.changed").value(true));
        assertThat(state(id)).isEqualTo(COMPLETED);
        for (int i = 0; i < 2; i++) {
            mvc.perform(patch("/v1/announcements/{id}/cancel-completion", id)
                            .header("X-User-Id", 10L))
                    .andExpect(status().isNoContent());
            assertThat(state(id)).isEqualTo(CLOSED);
        }
    }

    @Test
    void alreadyCompletedResponseReportsNoChange() throws Exception {
        Long id = save(COMPLETED);
        mvc.perform(patch("/v1/announcements/{id}/complete", id).header("X-User-Id", 10L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.changed").value(false));
        assertThat(state(id)).isEqualTo(COMPLETED);
    }

    @Test
    void repeatedCompletionReportsNoChange() {
        Long id = save(CLOSED);
        assertThat(service.completeAnnouncement(id, 10L).changed()).isTrue();
        assertThat(service.completeAnnouncement(id, 10L).changed()).isFalse();
    }

    @Test
    void otherOwnerCannotCompleteOrCompensate() {
        Long id = save(CLOSED);
        assertThatThrownBy(() -> service.completeAnnouncement(id, 20L)).isInstanceOf(RuntimeException.class);
        assertThat(state(id)).isEqualTo(CLOSED);
        service.completeAnnouncement(id, 10L);
        assertThatThrownBy(() -> service.cancelAnnouncementCompletion(id, 20L))
                .isInstanceOf(RuntimeException.class);
        assertThat(state(id)).isEqualTo(COMPLETED);
    }

    @Test
    void compensationDoesNotOverwriteAnotherState() throws Exception {
        Long id = save(OPEN);
        assertThatThrownBy(() -> service.cancelAnnouncementCompletion(id, 10L))
                .isInstanceOf(IllegalStateException.class);
        assertThat(state(id)).isEqualTo(OPEN);
        mvc.perform(patch("/v1/announcements/{id}/cancel-completion", id)
                        .header("X-User-Id", 10L))
                .andExpect(status().isConflict());
    }

    @Test
    void deletedAnnouncementCannotBeCompletedOrRestored() {
        Long id = save(DELETED);
        assertThatThrownBy(() -> service.completeAnnouncement(id, 10L)).isInstanceOf(RuntimeException.class);
        assertThatThrownBy(() -> service.cancelAnnouncementCompletion(id, 10L))
                .isInstanceOf(RuntimeException.class);
        assertThat(state(id)).isEqualTo(DELETED);
    }

    private Long save(AnnouncementStatus state) {
        return repository.saveAndFlush(Announcement.builder()
                .shelterId(10L).petId(5L).status(state).build()).getId();
    }

    private AnnouncementStatus state(Long id) {
        return repository.findById(id).orElseThrow().getStatus();
    }
}
