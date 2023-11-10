package com.ChesSEP.ChesSEP.Security.JWT;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    
    private final JwtAutenticationFilter jwtAutentificationFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
            .csrf((csrf) -> csrf.disable())
            .authorizeHttpRequests((authorizeHttpRequests)->authorizeHttpRequests.requestMatchers("/users/register","/users/authenticate", "/users/twoFactor").permitAll())
        	//.authorizeHttpRequests((authorizeHttpRequests)->authorizeHttpRequests.requestMatchers(HttpMethod.OPTIONS).permitAll())
            .authorizeHttpRequests((authorizeHttpRequests)->authorizeHttpRequests.anyRequest().authenticated())
            //.authorizeHttpRequests((authorizeHttpRequests)->authorizeHttpRequests.anyRequest().permitAll()) //For Testing
            .sessionManagement((sessionManagement)->sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authenticationProvider(authenticationProvider)
            .addFilterBefore(jwtAutentificationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}
