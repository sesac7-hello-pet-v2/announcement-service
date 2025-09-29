package hello.pet.announcementservice.dto.response;

import lombok.Data;

@Data
public class MyPetList {
    private String publicNoticeNumber;
    private String breed;
    private String gender;
    private int age;
    private String status;
    private Long Id;
    private boolean editable;   // 수정, 삭제 가능 여부
}
