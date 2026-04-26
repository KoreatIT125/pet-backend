package com.disaster.safety.util;

import java.util.Collection;
import java.util.Iterator;

import javax.validation.Valid;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.disaster.safety.user.SiteUser;
import com.disaster.safety.user.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/jwt-login")
public class JwtLoginController {
    
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @GetMapping(value = {"", "/"})
    public String home(Model model) {

        model.addAttribute("loginType", "jwt-login");
        model.addAttribute("pageName", "스프링 시큐리티 JWT 로그인");

        String loginId = SecurityContextHolder.getContext().getAuthentication().getName();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iter = authorities.iterator();
        GrantedAuthority auth = iter.next();
        String role = auth.getAuthority();

        SiteUser loginSiteUser = userService.getLoginSiteUserByUserId(loginId);
    
        if (loginSiteUser != null) {
            model.addAttribute("name", loginSiteUser.getUserId());
        }
        return "home";
    }

    @GetMapping("/join")
    public String joinPage(Model model) {
        
        model.addAttribute("loginType", "jwt-login");
        model.addAttribute("pageName", "스프링 시큐리티 JWT 로그인");

        // 회원가입을 하기위해 model -> joinRequest 전달
        model.addAttribute("joinRequest", new JoinRequest());
        return "join";
    }

    @PostMapping("/join")
    public String join(@Valid @ModelAttribute JoinRequest joinRequest,
                        BindingResult bindingResult, Model model) {
        
        model.addAttribute("loginType", "jwt-login");
        model.addAttribute("pageName", "스프링 시큐리티 JWT 로그인");                    
    
        // ID 중복 확인
        if (userService.checkUserIdDuplicate(joinRequest.getUserId())) {
            return "ID가 존재합니다.";
        }           
        
        // 비밀번호 확인
        if (!joinRequest.getPassword().equals(joinRequest.getPasswordCheck())) {
            return "비밀번호가 일치하지 않습니다.";
        }

        // 아무이상 없으면 joinRequest 통해서 회원가입
        userService.securityJoin(joinRequest);

        // 회원가입 후 메인화면으로
        return "redirect:/jwt-login";
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest loginRequest){

        SiteUser siteUser = userService.login(loginRequest);


        if(siteUser==null){
            return "ID 또는 비밀번호가 일치하지 않습니다!";
        }

        String token = jwtUtil.createJwt(siteUser.getUserId(), siteUser.getUserName(), 1000 * 60 * 60L);
        return token;
    }

    @GetMapping("/info")
    public String memberInfo(Authentication auth, Model model) {

        SiteUser siteUser = userService.getSiteUserByUserId(auth.getName());

        return "ID : " + siteUser.getUserId() + "\n이름 : " + siteUser.getUserName();
    }
    
    @GetMapping("/admin")
    public String adminPage(Model model) {

        return "인가 성공!";
    }
}
