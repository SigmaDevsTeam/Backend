package com.sigmadevs.testtask.security.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sigmadevs.testtask.app.entity.User;
import com.sigmadevs.testtask.security.entity.Role;
import com.sigmadevs.testtask.security.service.AuthService;
import com.sigmadevs.testtask.security.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    private final AuthService authService;
    private final UserService userService;
    private final JwtUtil jwtUtils;
    @Value("${spring.security.front-end-redirect-url-sign-in}")
    private String frontendUrl;
    private final OAuth2AuthorizedClientService authorizedClientService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
        String provider = oAuth2AuthenticationToken.getAuthorizedClientRegistrationId();
        if ("github".equals(provider) || "google".equals(provider)) {
            String username;
            DefaultOAuth2User principal = (DefaultOAuth2User) authentication.getPrincipal();
            Map<String, Object> attributes = principal.getAttributes();
            Map<String, Object> modifiableMap = new HashMap<>(attributes);
            String email = attributes.getOrDefault("email", "") != null ? attributes.getOrDefault("email", "").toString() : getPrimaryEmailForGitHub(authentication);
            modifiableMap.put("email", email);
            String name = attributes.getOrDefault("name", "").toString();
            if ("github".equals(oAuth2AuthenticationToken.getAuthorizedClientRegistrationId())) {
                username = attributes.getOrDefault("login", "").toString();
            } else if ("google".equals(oAuth2AuthenticationToken.getAuthorizedClientRegistrationId())) {
                username = email.split("@")[0];
            } else {
                throw new RuntimeException();//TODO
            }
            System.out.println("HELLO OAUTH: " + email + " : " + name + " : " + username);
            AtomicReference<User> main = new AtomicReference<>();
            userService.findByEmailOptional(email)
                    .ifPresentOrElse(user -> {
                        main.set(user);
                        DefaultOAuth2User oauthUser = new DefaultOAuth2User(
                                Collections.singleton(user.getRole()),
                                modifiableMap,
                                "email"
                        );
                        Authentication securityAuth = new OAuth2AuthenticationToken(
                                oauthUser,
                                oauthUser.getAuthorities(),
                                oAuth2AuthenticationToken.getAuthorizedClientRegistrationId()
                        );
//                        Cookie cookie = authService.createCookie(jwtUtils.generateToken(user));
//                        response.addCookie(cookie);
                        SecurityContextHolder.getContext().setAuthentication(securityAuth);
                    }, () -> {
                        User newUser = new User();
                        newUser.setRole(Role.USER);
                        newUser.setEmail(email);
                        newUser.setUsername(username);
                        User saved = userService.save(newUser, null);
                        main.set(saved);
                        DefaultOAuth2User oauthUser = new DefaultOAuth2User(
                                Collections.singleton(newUser.getRole()),
                                modifiableMap,
                                "email"
                        );
                        Authentication securityAuth = new OAuth2AuthenticationToken(
                                oauthUser,
                                oauthUser.getAuthorities(),
                                oAuth2AuthenticationToken.getAuthorizedClientRegistrationId()
                        );
                        SecurityContextHolder.getContext().setAuthentication(securityAuth);
                    });
            User user = main.get();
//            Cookie cookie = authService.createCookie(jwtUtils.generateToken(user));
//            response.addCookie(cookie);
            this.setAlwaysUseDefaultTargetUrl(true);
            this.setDefaultTargetUrl(frontendUrl+"?jwt="+jwtUtils.generateToken(user));
//            getRedirectStrategy().sendRedirect(request, response, frontendUrl);
            super.onAuthenticationSuccess(request, response, authentication);
        }
    }

    private String getPrimaryEmailForGitHub(Authentication authentication) throws JsonProcessingException {
        OAuth2AuthorizedClient client = authorizedClientService
                .loadAuthorizedClient(
                        ((OAuth2AuthenticationToken) authentication).getAuthorizedClientRegistrationId(),
                        authentication.getName());
        String accessToken = client.getAccessToken().getTokenValue();
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        String url = "https://api.github.com/user/emails";
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        String json = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        List<Map<String, Object>> emails = objectMapper.readValue(json, new TypeReference<>() {
        });
        //TODO
        return emails.stream()
                .filter(email -> Boolean.TRUE.equals(email.get("primary")))
                .map(email -> (String) email.get("email"))
                .findFirst()
                .orElseThrow();
    }
}