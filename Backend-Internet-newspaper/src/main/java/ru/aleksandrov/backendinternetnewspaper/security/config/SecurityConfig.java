package ru.aleksandrov.backendinternetnewspaper.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.aleksandrov.backendinternetnewspaper.security.JWT.AuthEntryPointJwt;
import ru.aleksandrov.backendinternetnewspaper.security.JWT.AuthTokenFilter;


@EnableWebSecurity
@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final AuthTokenFilter authTokenFilter;
    private final AuthEntryPointJwt unauthorizedHandler;

    @Autowired
    public SecurityConfig(AuthTokenFilter authTokenFilter, AuthEntryPointJwt unauthorizedHandler) {
        this.authTokenFilter = authTokenFilter;
        this.unauthorizedHandler = unauthorizedHandler;
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors().and().csrf(AbstractHttpConfigurer::disable)
                .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling((exception) -> exception
                        .authenticationEntryPoint(unauthorizedHandler))
                .authorizeHttpRequests((requests) -> requests
                        .antMatchers("/auth/sign-in", "/auth/sign-up", "/news/fresh-news",
                                "/news/user-themes", "/comment/show/**", "/comment/check-db").permitAll()
                        .anyRequest().authenticated())
                .addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}
