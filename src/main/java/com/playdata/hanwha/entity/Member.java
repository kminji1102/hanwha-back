    package com.playdata.hanwha.entity;

    import jakarta.persistence.*;
    import lombok.*;

    @Entity
    @Table(name = "Member")
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public class Member {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(nullable = true)
        private String imagePath;

        @Column(nullable = false, unique = true)
        private String nickname;

        @Column(nullable = false)
        private String password;
    }
