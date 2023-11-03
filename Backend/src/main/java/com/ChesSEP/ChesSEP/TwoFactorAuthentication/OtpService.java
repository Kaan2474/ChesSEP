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
            String otpString = randomOTP +"_"+ user.getId(); // ID mit anhängen, damit UNIQUE Zuweisung der 2FA möglich ist
            user.setTwoFactor(randomOTP);
            userRepository.save(user);
            emailService.sendOTP(user.getId(), "Ihr 2FA-Code: " + otpString);

            //Test
            lastOTP = otpString;
            return "Erfolgreich erstellt";
        } catch (Exception e) {
            e.printStackTrace();
            return "Fehlgeschlagen";
        }
    }


    // Testing Methode
    private String lastOTP;
    public String getLastOTP(){
        return lastOTP;
    }

}
