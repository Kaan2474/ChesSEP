package com.ChesSEP.ChesSEP.Emailtests;

import com.ChesSEP.ChesSEP.Email.EmailService;
import com.ChesSEP.ChesSEP.TwoFactorAuthentication.OtpService;
import com.ChesSEP.ChesSEP.User.User;
import com.ChesSEP.ChesSEP.User.UserRepository;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@TestPropertySource(properties = {
        "spring.jpa.defer-database-initialization=true",
        "spring.jpa.hibernate.ddl.auto=create-drop",
        "spring.jpa.properties.hibernate.globally_quoted_identifiers=true"

})

public class EmailTests {


    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OtpService otpService;


    @Test
    @Order(1)
    public void emailService_SendMail_ReceiveMail() {
        //Arrange
        User testUser = User.builder()
                .id(1L)
                .email("mario-mai@gmx.net")
                .vorname("Mario")
                .nachname("Mai")
                .passwort("12345")
                .build();
        userRepository.save(testUser);

        User testUser2 = User.builder()
                .id(2L)
                .email("testzweckeio@gmail.com")
                .vorname("Jonas")
                .nachname("Mai")
                .build();
        userRepository.save(testUser2);


        //Act

        emailService.send(1L, 2L, "TEST 1 Freundschaftsanfrage", " hiii");
        boolean status_email = emailService.sendSuccessfully();

        //Assert
        assertTrue(status_email);
        //   + Eine Email im Postfach von testzweckeio@gmail.com :)
    }

    @Test
    @Order(2)
    public void emailService_SendEmail_ReceiveEmail_UserDetails() { //Freundschaftsanfrage mit User-Details
        //Arrange
        User testUser1 = User.builder()
                .vorname("Mario")
                .nachname("Mai")
                .email("mario-mai@gmx.net")
                .build();

        User testUser2 = User.builder()
                .vorname("Max")
                .nachname("Mustermann")
                .email("testzweckeio@gmail.com")
                .build();

        userRepository.save(testUser1);
        userRepository.save(testUser2);


        //Act
        emailService.send(testUser1.getId(), testUser2.getId(),
                "TEST 2 Freundschaftsanfrage",
                testUser1.getVorname() + " " + testUser1.getNachname() + " möchte mit Ihnen befreundet sein.");

        boolean status_email = emailService.sendSuccessfully(); // soll true ausgeben
        //Assert -
        assertTrue(status_email);
        // + Nette Email im Postfach von testzweckeio@gmail.com mit User_ID Vor+ und Nachnamen



    }

    @Test
    @Order(3)

    public void emailService_handlingError() {
        //Arrange
        User testUser1 = User.builder()
                .vorname("Mario")
                .nachname("Mai")
                .email("testzweckeio@gmail.com")
                .build();

        User testUser2 = User.builder()
                .vorname("Max")
                .nachname("Mustermann")
                .email("email die nicht legit ist aber trotzdem in der DB vorhanden")
                .build();

        userRepository.save(testUser1);
        userRepository.save(testUser2);

        //Act

        emailService.send(testUser1.getId(), testUser2.getId(),
                "TEST 3 Freundschaftsanfrage",
                testUser1.getVorname() + " " + testUser1.getNachname() + " möchte mit Ihnen befreundet sein.");
        boolean status_email = emailService.sendSuccessfully();
        //Arrange
        assertFalse(status_email);
        //MailException und eine Email auf Email vom Sender (testUser1) -> testzweckeio@gmail.com

    }

    @Test
    @Order(4)

    public void emailService_sendOTP() throws Exception{
        //Arrange
        User testUser = User.builder()
                .id(1L)
                .vorname("Mario")
                .nachname("Mai")
                .email("testzweckeio@gmail.com")
                .twoFactor(999999)
                .build();

        User testUser1 = User.builder()
                .id(1L)
                .vorname("Mario")
                .nachname("Mai")
                .email("testzweckeio@gmail.com")
                .twoFactor(999999)
                .build();
        userRepository.save(testUser);
        userRepository.save(testUser1);


        otpService.generateOTP(testUser);
        String msg = "Dein 2FA " + testUser.getTwoFactor();
        boolean email_status;
        boolean check_otp = testUser.getTwoFactor() == Integer.parseInt(otpService.getLastOTP());

        //Act

        emailService.sendOTP(1L,msg);
        email_status = emailService.sendSuccessfully();

        //Assert
        assertTrue(email_status); //Email wurde versendet
        assertTrue(check_otp); //OTP welches verschickt wurde stimmt mit twoFactor vom User überein
    }


}


