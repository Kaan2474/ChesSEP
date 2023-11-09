package com.ChesSEP.ChesSEP.FriendListtests;

import com.ChesSEP.ChesSEP.Friendlist.*;
import com.ChesSEP.ChesSEP.Security.JWT.TokenService;
import com.ChesSEP.ChesSEP.Security.RequestHolder.AuthUserRequestHolder;
import com.ChesSEP.ChesSEP.User.Role;
import com.ChesSEP.ChesSEP.User.User;
import com.ChesSEP.ChesSEP.User.UserRepository;
import com.ChesSEP.ChesSEP.User.UserService;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@TestPropertySource(properties = {
        "spring.jpa.defer-database-initialization=true",
        "spring.jpa.hibernate.ddl.auto=create-drop",
        "spring.jpa.properties.hibernate.globally_quoted_identifiers=true"

})
@RunWith(SpringRunner.class)
public class FriendListtests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private FriendRepository friendRepository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private FriendService friendService;



    @Test
    @Order(1)
    public void sendFriendRequestTest(){
        //Arrange
        User testUser = User.builder()
                .email("mario-mai@gmx.net")
                .vorname("Mario")
                .nachname("Mai")
                .passwort("12345")
                .role(Role.USER)
                .build();

        userRepository.save(testUser);

        User testUser2 = User.builder()
                .email("testzweckeio@gmail.com")
                .vorname("Jonas")
                .nachname("Mai")
                .role(Role.USER)
                .build();

        userRepository.save(testUser2);

        String token = userService.checkTwoFactor(new AuthUserRequestHolder("mario-mai@gmx.net", "12345", 1111));

        //Act
        friendService.sendFriendRequest("Bearer "+token, testUser2.getEmail());

        //Assert
        Assertions.assertTrue(friendRepository.searchRequest(testUser.getId(),testUser2.getId()) != null);
    }

    @Test
    @Order(2)
    public void acceptFriendRequestTest(){
        //Arrange
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

        //Act
        friendService.acceptFriendRequest(tokenService.GenerateToken(testUser), testUser2.getId());
    }
}
