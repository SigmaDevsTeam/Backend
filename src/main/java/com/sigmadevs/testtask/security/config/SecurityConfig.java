package com.sigmadevs.testtask.security.config;

import com.sigmadevs.testtask.security.entity.Role;
import com.sigmadevs.testtask.security.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.server.CookieSameSiteSupplier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserService userService;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    private final JwtFilter jwtFilter;


    @Value("${spring.security.front-end-redirect-url-sign-in}")
    private String redirectUrlLogin;
    @Value("${spring.security.front-end-redirect-url-logout}")
    private String redirectUrlLogout;


    @SneakyThrows
    @Bean
    public SecurityFilterChain getFilterChain(HttpSecurity http) {
        http.
                csrf(AbstractHttpConfigurer::disable);
//        csrf(csrf ->
//        csrf.csrfTokenRepository(new CookieCsrfTokenRepository())
//                .ignoringRequestMatchers("/login","/registration")
//                .csrfTokenRequestHandler(new SpaCsrfTokenRequestHandler()) );


        return http.
                sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)).
                cors(Customizer.withDefaults()).
                exceptionHandling(e -> e.authenticationEntryPoint(new AuthenticationEntryPoint())).
                oauth2Login(e -> e.
//                        loginPage("/oauth2/authorization/google").
                        successHandler(oAuth2LoginSuccessHandler)).
                authorizeHttpRequests(request ->
                        request.
                                requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/avatars/**", "/logout", "/login/**", "/registration", "/static/**", "/api/**", "/oauth2/**").permitAll().
                                requestMatchers(HttpMethod.GET,"/api/**").permitAll().
                                requestMatchers("/api/**").authenticated().
                                requestMatchers("/admin/**").hasAuthority(Role.ADMIN.getAuthority()).
                                anyRequest().authenticated()).
                logout(e -> e
                        .logoutUrl("/logout")
                        .deleteCookies("accessToken", "JSESSIONID").logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .logoutSuccessHandler((request, response, authentication) -> {
                            response.setStatus(HttpServletResponse.SC_OK);
                            response.getWriter().write("Logged out successfully");
//                            response.sendRedirect(redirectUrlLogout);
                        })
                ).
                userDetailsService(userService).
//                requiresChannel(channel ->
//                channel.anyRequest().requiresSecure()).
                addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class).
                build();
    }

//    @Bean
//    public CookieSameSiteSupplier applicationCookieSameSiteSupplier() {
//        return CookieSameSiteSupplier.ofLax();
//    }
}