package com.ChesSEP.ChesSEP.Email;

import com.ChesSEP.ChesSEP.User.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import org.springframework.stereotype.Service;


@Service //logic-layer
@AllArgsConstructor //Generate Constructor for JavaMailSender with @NonNull checking on these parameters - ohne muss Konstruktor angegeben werden
public class EmailService implements EmailSender { // "EmailService" im Klassendiagramm = MailSender

    @Autowired
    private final JavaMailSender mailSender; //API to send an Email
    @Autowired
    private final UserService userService;

    @Override
    public void send(Long user_id, Long to, String subject, String msg) throws MailException {
        try {
            SimpleMailMessage message = new SimpleMailMessage(); //SimpleMailMessage for simple Email with only text
            message.setTo(userService.findUserById(to).getEmail()); //hier muss Email -> ggf. Fremdschlüssel von FreundID - Emailadresse
            message.setSubject(subject);
            message.setText(msg);

            mailSender.send(message);
            handleEmailSuccessfully();
        }
        catch (MailException e){
            String error = "Email konnte nicht zugestellt werden.";
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(userService.findUserById(user_id).getEmail());
            message.setSubject("Konnte nicht zugestellt werden");
            message.setText(error);

            mailSender.send(message);
            handleEmailError(e);
        }
    }

    private void handleEmailSuccessfully(){
        System.out.println("Email wurde erfolgreich versendet");
    }

    private void handleEmailError(MailException e){
        System.out.println("Email konnte nicht zugestellt werden. " + e.getMessage());
    }

}

