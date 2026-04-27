package com.disaster.safety.member.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.disaster.safety.member.dto.LoginRequestDto;
import com.disaster.safety.member.dto.MemberRequestDto;
import com.disaster.safety.member.entity.Member;
import com.disaster.safety.member.service.MemberService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/login")
    public ResponseEntity<String> getMemberProfile(@Valid @RequestBody LoginRequestDto request) {
        String token = memberService.login(request);
        return ResponseEntity.status(HttpStatus.OK).body(token);
    }

    @PostMapping("/signup")
    public ResponseEntity<Long> signup(@Valid @RequestBody MemberRequestDto member) {
        Member entity = new Member();
        entity.setUserId(member.getUserId());
        entity.setUserName(member.getUserName());
        entity.setPassword(member.getPassword());
        entity.setRole(member.getRole());

        Long id = memberService.signup(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }
}
