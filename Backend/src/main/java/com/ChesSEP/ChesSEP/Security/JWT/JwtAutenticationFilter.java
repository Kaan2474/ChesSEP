package com.ChesSEP.ChesSEP.Security.JWT;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.micrometer.common.lang.NonNull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAutenticationFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,@NonNull HttpServletResponse response,@NonNull  FilterChain filterChain)throws ServletException, IOException {
        
        final String requestHeader = request.getHeader("Authorization");

        if(requestHeader ==null||!requestHeader.startsWith("Bearer ")){
            filterChain.doFilter(request, response);
            return;
        }

        final String jsonWebToken = requestHeader.substring(7);

        final String userEmail= tokenService.extractEmail(jsonWebToken);

        if(userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null){
            //retreive Details from Database
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

            if(tokenService.isTokenValid(userDetails, jsonWebToken)){

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userDetails, 
                    null, // no credentials implemented
                    userDetails.getAuthorities());

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                //Update Securty Context Holder
                SecurityContextHolder.getContext().setAuthentication(authToken);
                
            }
        }
        filterChain.doFilter(request, response);
    }
    
}
