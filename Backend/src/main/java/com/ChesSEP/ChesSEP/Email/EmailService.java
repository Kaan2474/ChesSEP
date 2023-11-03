package com.ChesSEP.ChesSEP.Email;

import com.ChesSEP.ChesSEP.User.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import org.springframework.stereotype.Service;


@Service //logic-layer
@RequiredArgsConstructor //Generate Constructor for FINAL
public class EmailService implements EmailSender { // "EmailService" im Klassendiagramm = MailSender

    @Autowired
    private final JavaMailSender mailSender; //API to send an Email
    @Autowired
    private final UserRepository userRepository;

    @Override
    public void send(Long user_id, Long to, String subject, String msg) throws MailException {
        try {
            SimpleMailMessage message = new SimpleMailMessage(); //SimpleMailMessage for simple Email with only text
            message.setTo(userRepository.findUserById(to).getEmail()); //hier muss Email -> ggf. Fremdschlüssel von FreundID - Emailadresse
            message.setSubject(subject);
            message.setText(msg);

            mailSender.send(message);
        }
        catch (MailException e){
            String error = "Email konnte nicht zugestellt werden.";
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(userRepository.findUserById(user_id).getEmail());
            message.setSubject("Konnte nicht zugestellt werden");
            message.setText(error);

            mailSender.send(message);
            handleEmailError(e);
        }
    }

    @Override
    public void sendOTP(Long user_id, String msg) {
        try{
            SimpleMailMessage twoFA = new SimpleMailMessage();
            twoFA.setTo(userRepository.findUserById(user_id).getEmail());
            twoFA.setSubject("Dein 2FA Code");
            twoFA.setText(msg);

            mailSender.send(twoFA);

            //Test
            check = true;

        }catch (MailException e) {
            String error = "Email konnte nicht zugestellt werden.";
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(userRepository.findUserById(user_id).getEmail());
            message.setSubject("Konnte nicht zugestellt werden");
            message.setText(error);

            mailSender.send(message);
            handleEmailError(e);
            check = false;
        }
    }

    private void handleEmailError(MailException e){
        System.out.println("Email konnte nicht zugestellt werden. " + e.getMessage());
    }


    //Test
    public boolean sendSuccessfully(){
        return check;
    }

    private boolean check;
}

