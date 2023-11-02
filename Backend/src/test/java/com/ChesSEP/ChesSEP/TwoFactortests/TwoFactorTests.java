package com.ChesSEP.ChesSEP.TwoFactortests;

import com.ChesSEP.ChesSEP.TwoFactorAuthentication.OtpService;
import com.ChesSEP.ChesSEP.User.User;
import com.ChesSEP.ChesSEP.User.UserRepository;
import org.junit.jupiter.api.*;
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
public class TwoFactorTests {


    @Autowired
    private UserRepository userRepository; //Um auf Funktion userRepo zuzugreifen
    @Autowired
    private OtpService otpService;


    @Test
    @Order(1)

    public void otpService_send_get_EmailWithOTP(){
        //Arrange
        User testUser = User.builder()
                .email("testzweckeio@gmail.com")
                .build();
        userRepository.save(testUser);

        //act
        otpService.generateOTP(testUser);

        //Assert
        Assertions.assertEquals(otpService.getLastOTP(), testUser.getTwoFactor());



        //

    }


}
