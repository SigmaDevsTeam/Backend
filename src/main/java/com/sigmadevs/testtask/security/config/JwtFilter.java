package com.sigmadevs.testtask.security.config;

import com.sigmadevs.testtask.app.entity.User;
import com.sigmadevs.testtask.app.exception.UserNotFoundException;
import com.sigmadevs.testtask.security.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
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

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwt;
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            jwt = bearerToken.substring(7);
        } else jwt = null;

        if (jwt != null && !jwt.isBlank() && jwtUtil.validateToken(jwt)) {
            User user;
            try {

                user = userService.findByUsername(jwtUtil.getUsername(jwt));
            } catch (UserNotFoundException e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("User not found");
//                response.setHeader("Set-Cookie", "accessToken=unauthorized;expires=Thu, 01 Jan 1970 00:00:01 GMT;");
                filterChain.doFilter(request, response);
                return;
            }
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

    }

}