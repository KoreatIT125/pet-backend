package com.disaster.safety.util;


import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.disaster.safety.user.CustomUserDetails;
import com.disaster.safety.user.SiteUser;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    
    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // request -> Authorization 헤더 찾기
        String authorization = request.getHeader("Authorization");

        if(authorization == null || !authorization.startsWith("Bearer ")) {

            System.out.println("token null");
            // 유효하지 않은 토큰 request, response를 다음 필터로
            filterChain.doFilter(request, response);

            return;
        }
        // Bearer 접두사 삭제
        String token = authorization.split(" ")[1];

        // token 유효기간 검증
        if(jwtUtil.isExpired(token)) {
            System.out.println("token expired");
            filterChain.doFilter(request, response);

            return;
        }
        // 최종 token 검증 완료시
        String userId = jwtUtil.getUserId(token);
        //String userRole = jwtUtil.getUserRole(token);

        SiteUser siteUser = new SiteUser();
        siteUser.setUserId(userId);

        siteUser.setPassword("임시 비번");
        //siteUser.setUserRole(userRole);

        // UserDetails에 회원 정보 객체 담기
        CustomUserDetails customUserDetails = new CustomUserDetails(siteUser);

        // 스프링 시큐리티 인증 토큰 생성
        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
    
        // 세션에 사용자 등록 => 일시적으로 user 세션 생성
        SecurityContextHolder.getContext().setAuthentication(authToken);

        // 다음 필터로 request, response 넘겨줌
        filterChain.doFilter(request, response);
    }
}