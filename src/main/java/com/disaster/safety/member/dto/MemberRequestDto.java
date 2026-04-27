package com.disaster.safety.member.dto;

import javax.validation.constraints.NotBlank;

import com.disaster.safety.member.entity.RoleType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberRequestDto {
    
    @NotBlank(message = "ID는 필수 입력 값입니다.")
    private String userId;

    @NotBlank(message = "이름은 필수 입력 값입니다.")
    private String userName;

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    private String password;

    private RoleType role;
}
