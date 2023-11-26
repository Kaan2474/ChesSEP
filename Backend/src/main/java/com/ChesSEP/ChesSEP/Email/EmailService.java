package com.ChesSEP.ChesSEP.Email;

import com.ChesSEP.ChesSEP.User.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import org.springframework.stereotype.Service;


@Service //logic-layer
@RequiredArgsConstructor
public class EmailService implements EmailSender {

    private final JavaMailSender mailSender;
    private final UserRepository userRepository;

    private final String Email="testzweckeio@gmail.com";

    @Override
    public void send(Long user_id, Long to, String subject, String msg) throws MailException {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(userRepository.findUserById(to).getEmail());
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

