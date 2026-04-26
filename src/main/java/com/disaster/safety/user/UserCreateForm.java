package com.disaster.safety.user;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

// 유저 회원가입 dto

@Getter
@Setter
public class UserCreateForm {
    @Size(min = 6, max = 16)
    @NotEmpty(message = "ID를 입력해주시기 바랍니다.")
    private String userId;

    @Size(min = 6, max = 16)
    @NotEmpty(message = "비밀번호를 입력해주시기 바랍니다.")
    private String password1;

    @Size(min = 6, max = 16)
    @NotEmpty(message = "비밀번호를 재입력해주시기 바랍니다.")
    private String password2;

    @NotEmpty(message = "이름을 입력해주시기 바랍니다.")
    private String userName;
    
    // builder 방식
    public SiteUser toEntity(){
        return SiteUser.builder()
                .userId(this.userId)
                .password(this.password1)
                .userName(this.userName)
                //.role(UserRole.USER)
                .build();
    }
} 