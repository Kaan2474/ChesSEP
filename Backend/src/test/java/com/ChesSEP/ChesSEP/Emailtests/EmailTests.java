package com.ChesSEP.ChesSEP.Emailtests;

import com.ChesSEP.ChesSEP.Email.EmailService;
import com.ChesSEP.ChesSEP.User.User;
import com.ChesSEP.ChesSEP.User.UserRepository;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;


@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@TestPropertySource(properties = {
        "spring.jpa.defer-database-initialization=true",
        "spring.jpa.hibernate.ddl.auto=create-drop",
        "spring.jpa.properties.hibernate.globally_quoted_identifiers=true"

})

public class EmailTests{


    @Autowired
    private EmailService emailService; // um die Funkion des EmailService zu testen und nutzen

    @Autowired
    private UserRepository userRepository; // um die Funktion des UserRepo zu testen und nutzen


    @Test
    @Order(1)
    public void emailService_SendMail_ReceiveMail(){
        //Arrange - Selbst inzinierte Daten
        User testUser = User.builder()
                .email("mario-mai@gmx.net")
                .vorname("Mario")
                .nachname("Mai")
                .passwort("12345")
                .build();

        userRepository.save(testUser);

        User testUser2 = User.builder()
                .email("testzweckeio@gmail.com")
                .vorname("Jonas")
                .nachname("Mai")
                .build();

        userRepository.save(testUser2);


        //Act - Was wird getestet
        emailService.send(1L, 2L, "Freundschaftsanfrage", " FU");

        //Assert - Was wollen wir bekommen
        //   - Eine Email im Postfach :)
    }

    @Test
    @Order(2)
    public void emailService_SendEmail_ReceiveEmail_UserDetails(){
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
                "Freundschaftsanfrage",
                testUser1.getVorname() + " " + testUser1.getNachname() + " möchte mit Ihnen befreundet sein.");

        //Assert -
        // Nette Email im Postfach mit User_ID Vor und Nachnamen


    }

    @Test
    @Order(3)

    public void emailService_handlingError(){
        //Arrange
        User testUser1 = User.builder()
                .vorname("Mario")
                .nachname("Mai")
                .email("mario-mai@gmx.net")
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
                "Freundschaftsanfrage",
                testUser1.getVorname() + " " + testUser1.getNachname() + " möchte mit Ihnen befreundet sein.");
    }
        //Arrange -

        // String Output "Email konnte nicht zugestellt werden " MailException und eine Email auf Email von User_ID


}

