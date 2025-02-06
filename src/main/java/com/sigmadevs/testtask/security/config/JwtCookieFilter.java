package com.sigmadevs.testtask.security.config;

import com.sigmadevs.testtask.app.User;
import com.sigmadevs.testtask.security.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.Principal;
import java.util.Arrays;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtCookieFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Cookie cookie = Arrays.stream(Optional.ofNullable(request.getCookies()).orElse(new Cookie[]{}))
                .filter(cookieElement -> "accessToken".equals(cookieElement.getName()))
                .findFirst().orElse(null);
        if (cookie != null) {
            String jwtToken = Optional.ofNullable(cookie.getValue()).orElse("");
            if (!jwtToken.isBlank() && jwtUtil.validateToken(jwtToken)) {
                User user = userService.findByUsername(jwtUtil.getUsername(jwtToken));
                if (user != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(new Principal() {
                        @Override
                        public String getName() {
                            return user.getUsername();
                        }
                    },
                            null,
                            user.getAuthorities()
                    );
                    SecurityContextHolder.getContext().setAuthentication(token);
                }
                filterChain.doFilter(request, response);
                return;
            } else {
                log.debug("accessToken value is blank");
                filterChain.doFilter(request, response);
                return;
            }
        } else {
            log.debug("no accessToken ");
            filterChain.doFilter(request, response);
        }
    }
}