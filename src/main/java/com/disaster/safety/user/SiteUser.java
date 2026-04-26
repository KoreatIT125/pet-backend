package com.disaster.safety.user;

//import javax.persistence.*;
import javax.persistence.Column;
import javax.persistence.Entity;
//import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
public class SiteUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 유저 이름
    @Column(name = "userName", nullable = false)
    private String userName;

    // 유저 ID
    @Column(name = "userId", unique = true, nullable = false)
    private String userId;

    // 유저 비밀번호
    @Column(name = "password", nullable = false)
    private String password;
    
    // 유저 권한 -> DB 컬럼에 없어서 잠시 주석
    //@Enumerated(EnumType.STRING)
    //private UserRole role;
    
 }