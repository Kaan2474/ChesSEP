package com.ChesSEP.ChesSEP.TwoFactorAuthentication;

import com.ChesSEP.ChesSEP.User.User;
import com.ChesSEP.ChesSEP.User.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Deprecated // Redirecting wird durchs Frontend erzeugt, deswegen Deprecated
@Component
public class SuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    UserRepository userRepository; //um Email zu erfahren
    @Autowired
    OtpService otpService;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        String redirectURL = null;
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        User user = userRepository.findByEmail(username);
        String output = otpService.generateOTP(user);
        if(output == "Erfolgreich erstellt"){
            redirectURL="";

            new DefaultRedirectStrategy().sendRedirect(request, response, redirectURL);
        }
    }
}
