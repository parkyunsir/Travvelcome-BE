//package com.example.backend.model;
//
//import jakarta.persistence.*;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import java.util.Date;
//import java.util.HashMap;
//
//@Data
//@Builder
//@NoArgsConstructor
//@Entity
//@Table(name = "users")
//public class UserEntity {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(unique = true, nullable = false)
//    private String username;
//
//    @Column(nullable = false)
//    private String password;
//
//    @Column
//    private Boolean hasSignedUp;
//
//    @Column
//    private Date connectedAt;
//
//    @Column
//    private Date synchedAt;
//
//    @ElementCollection
//    @Column(name = "properties")
//    private HashMap<String, String> properties;
//
//    @Embedded
//    private KakaoAccount kakaoAccount;
//
//    @Embedded
//    private Partner partner;
//
//    // Nested classes to be embedded
//    @Embeddable
//    @Data
//    @NoArgsConstructor
//    public static class KakaoAccount {
//        @Column
//        private Boolean isProfileAgree;
//
//        @Column
//        private Boolean isNickNameAgree;
//
//        @Column
//        private Boolean isProfileImageAgree;
//
//        @Embedded
//        private Profile profile;
//
//        @Column
//        private Boolean isNameAgree;
//
//        @Column
//        private String name;
//
//        @Column
//        private Boolean isEmailAgree;
//
//        @Column
//        private Boolean isEmailValid;
//
//        @Column
//        private Boolean isEmailVerified;
//
//        @Column
//        private String email;
//
//        @Column
//        private Boolean isAgeAgree;
//
//        @Column
//        private String ageRange;
//
//        @Column
//        private Boolean isBirthYearAgree;
//
//        @Column
//        private String birthYear;
//
//        @Column
//        private Boolean isBirthDayAgree;
//
//        @Column
//        private String birthDay;
//
//        @Column
//        private String birthDayType;
//
//        @Column
//        private Boolean isGenderAgree;
//
//        @Column
//        private String gender;
//
//        @Column
//        private Boolean isPhoneNumberAgree;
//
//        @Column
//        private String phoneNumber;
//
//        @Column
//        private Boolean isCIAgree;
//
//        @Column
//        private String ci;
//
//        @Column
//        private Date ciCreatedAt;
//
//        @Embeddable
//        @Data
//        @NoArgsConstructor
//        public static class Profile {
//            @Column
//            private String nickName;
//
//            @Column
//            private String thumbnailImageUrl;
//
//            @Column
//            private String profileImageUrl;
//
//            @Column
//            private String isDefaultImage;
//
//            @Column
//            private Boolean isDefaultNickName;
//        }
//    }
//
//    @Embeddable
//    @Data
//    @NoArgsConstructor
//    public static class Partner {
//        @Column
//        private String uuid;
//    }
//}
