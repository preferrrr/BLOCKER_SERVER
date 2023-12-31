package com.blocker.blocker_server.config;

import com.blocker.blocker_server.jwt.JwtAuthenticationFilter;
import com.blocker.blocker_server.jwt.JwtExceptionFilter;
import com.blocker.blocker_server.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtProvider jwtProvider;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf((csrf) -> csrf.disable())
                .cors((cors) -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement((sessionManagement) -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authR -> {

                    //User
                    authR.requestMatchers("/users/login").permitAll();
                    authR.requestMatchers("/users/reissue-token").permitAll();

                    //Board
                    authR.requestMatchers(HttpMethod.GET, "/boards").hasAuthority("USER");
                    authR.requestMatchers("/boards/{boardId}").hasAuthority("USER");
                    authR.requestMatchers(HttpMethod.POST, "/boards").hasAuthority("USER");

                    //Signature
                    authR.requestMatchers(HttpMethod.POST,"/signatures").hasAuthority("GUEST");

                    //Image
                    authR.requestMatchers(HttpMethod.POST, "/images").hasAuthority("USER");

                    //Bookmark
                    authR.requestMatchers(HttpMethod.POST, "/bookmarks").hasAuthority("USER");
                    authR.requestMatchers(HttpMethod.DELETE, "/bookmarks/{boardId}").hasAuthority("USER");

                    //Contract
                    authR.requestMatchers("/contracts").hasAuthority("USER");
                    authR.requestMatchers("/contracts/{contractId}").hasAuthority("USER");



                })
                .addFilterBefore(new JwtAuthenticationFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JwtExceptionFilter(), JwtAuthenticationFilter.class);

        return http.build();

    }



    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("*");
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        configuration.addExposedHeader("*"); // CORS 문제, 포스트맨에는 보이지만 클라이언트에서 안 보이는거 해결
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}
