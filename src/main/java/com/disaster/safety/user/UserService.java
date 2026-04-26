package com.disaster.safety.user;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // NoArgsConstructor 방식
    public SiteUser create(String userId, String password, String userName) {
        SiteUser user = new SiteUser();
        user.setUserId(userId);
        user.setPassword(passwordEncoder.encode(password));
        user.setUserName(userName);
        //user.setRole(UserRole.USER);

        this.userRepository.save(user);
        return user;
    }
}
