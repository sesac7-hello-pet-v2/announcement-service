package hello.pet.announcementservice.dto.response;

// 보호소 관리자가 등록한 동물 목록
public class MyPetList {
    private String publicNoticeNumber;
    private String breed;
    private String gender;
    private int age;
    private String status;
    private Long Id;
    private boolean editable;   // 수정, 삭제 가능 여부
}
