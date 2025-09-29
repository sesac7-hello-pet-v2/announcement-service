package hello.pet.announcementservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@Table(name = "pets")
@NoArgsConstructor
@AllArgsConstructor
public class Pet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AnimalType animalType;

    @Column(nullable = false)
    private String gender;

    @Column(nullable = false)
    private String health;

    @Column(nullable = false)
    private String personality;

    @Column(nullable = false)
    private Integer age;

    @Column(nullable = false)
    private String breed;

    @Column
    private String imageUrl;

    public void updateInfo(String breed, String gender, int age, String health, String personality, String imageUrl,
                           AnimalType animalType) {
        this.breed = breed;
        this.gender = gender;
        this.age = age;
        this.health = health;
        this.personality = personality;
        this.imageUrl = imageUrl;
        this.animalType = animalType;
    }
}
