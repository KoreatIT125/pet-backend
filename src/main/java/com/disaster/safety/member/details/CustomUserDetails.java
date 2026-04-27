package com.disaster.safety.member.details;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.disaster.safety.member.dto.CustomUserInfoDto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

// 스프링 시큐리티의 userDetails 인터페이스를 구현하여 사용자 정보를
// SecurityContext에 자용할 수 있도록 제공

@Getter
@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

	private final CustomUserInfoDto member;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<String> roles = new ArrayList<>();
		roles.add("ROLE_" + member.getRole().toString());

		return roles.stream()
			.map(SimpleGrantedAuthority::new)
			.toList();
	}

	@Override
	public String getPassword() {
		return member.getPassword();
	}

	@Override
	public String getUsername() {
		return member.getUserId();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}