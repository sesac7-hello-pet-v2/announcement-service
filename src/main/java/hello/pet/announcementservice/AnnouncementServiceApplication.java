package hello.pet.announcementservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class AnnouncementServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AnnouncementServiceApplication.class, args);
    }

}
