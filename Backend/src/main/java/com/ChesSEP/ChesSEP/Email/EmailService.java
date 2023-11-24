package com.ChesSEP.ChesSEP.Email;

import com.ChesSEP.ChesSEP.User.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import org.springframework.stereotype.Service;


@Service //logic-layer
@RequiredArgsConstructor //Generate Constructor for FINAL
public class EmailService implements EmailSender { // "EmailService" im Klassendiagramm = MailSender

    private final JavaMailSender mailSender; //API to send an Email
    private final UserRepository userRepository;

    private final String Email="testzweckeio@gmail.com";

    @Override
    public void send(Long user_id, Long to, String subject, String msg) throws MailException {
            SimpleMailMessage message = new SimpleMailMessage(); //SimpleMailMessage for simple Email with only text
            message.setTo(userRepository.findUserById(to).getEmail()); // durch findUserById(to) wird der gesuchte User projiziert und .getEmail() gibt die Email aus
            message.setSubject(subject);
            message.setFrom(Email);
            message.setText(msg);

            mailSender.send(message);
            check = true;
    }

    @Override
    public void sendOTP(Long user_id, String msg) {
            SimpleMailMessage twoFA = new SimpleMailMessage();
            twoFA.setTo(userRepository.findUserById(user_id).getEmail());
            twoFA.setSubject("Dein 2FA Code");
            twoFA.setText(msg);
            twoFA.setFrom(Email);

            mailSender.send(twoFA);

            //Test
            check = true;
    }

    //Test
    public boolean sendSuccessfully(){
        return check;
    }

    private boolean check;
}

