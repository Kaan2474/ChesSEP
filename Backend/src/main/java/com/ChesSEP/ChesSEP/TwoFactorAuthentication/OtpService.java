package com.ChesSEP.ChesSEP.TwoFactorAuthentication;

import com.ChesSEP.ChesSEP.Email.EmailService;
import com.ChesSEP.ChesSEP.User.User;
import com.ChesSEP.ChesSEP.User.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class OtpService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmailService emailService;

    //2FA - OTP erzeugen
    public String generateOTP(User user) { //String Methode, damit SuccessHandler ein Rückgabewert bekommt
        try {
            SecureRandom random = new SecureRandom(); // PRNG - pseudo-random generated number
            int randomOTP = Math.abs(random.nextInt(100000)); // muss eine natürliche Zahl sein
            String otpString = String.valueOf(randomOTP) +"_"+ user.getId(); //muss zum String gecastet werden, da .setText in SimpleMailMessage String sein muss
            user.setTwoFactor(randomOTP);
            userRepository.save(user); //damit OTP in DB -> muss OTP also als Column deklariert werden???
            emailService.sendOTP(user.getId(), "Ihr 2FA-Code: " + otpString
                    );
            //Test
            lastOTP = Integer.parseInt(otpString);
            return "Erfolgreich erstellt";
        } catch (Exception e) {
            e.printStackTrace();
            return "Fehlgeschlagen";
        }
    }


    // Testing Methode
    private int lastOTP;
    public int getLastOTP(){
        return lastOTP;
    }

}
