package com.ChesSEP.ChesSEP.ChessGameTest;

import com.ChesSEP.ChesSEP.ChessGame.ChessGame;
import com.ChesSEP.ChesSEP.ChessGame.ChessgameRepository;
import com.ChesSEP.ChesSEP.ChessGame.MatchRequestRepository;
import com.ChesSEP.ChesSEP.ChessGame.MatchmakingService;
import com.ChesSEP.ChesSEP.Security.JWT.TokenService;
import com.ChesSEP.ChesSEP.Security.RequestHolder.UserRequestHolder;
import com.ChesSEP.ChesSEP.User.User;
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
@RunWith(SpringRunner.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@TestPropertySource(properties = {
        "spring.jpa.defer-database-initialization=true",
        "spring.jpa.hibernate.ddl.auto=create-drop",
        "spring.jpa.properties.hibernate.globally_quoted_identifiers=true"
})

public class ChessGameTest {
    @Autowired
    private ChessgameRepository chessgameRepository;

    @Autowired
    private MatchRequestRepository matchRequestRepository;

    @Autowired
    private MatchmakingService matchmakingService;

    @Autowired
    UserService userService;

    @Autowired
    private TokenService tokenService;

    @Test
    @Order(1)
    public void queueMatch(){
        //Arrange
        UserRequestHolder testUser = UserRequestHolder.builder()
                .email("mario-mai@gmx.net")
                .vorname("Mario")
                .nachname("Mai")
                .passwort("12345")
                .build();

        userService.registerUser(testUser);

        UserRequestHolder testUser2 = UserRequestHolder.builder()
                .email("testzweckeio@gmail.com")
                .vorname("Jonas")
                .nachname("Mai")
                .passwort("12345")
                .build();

        userService.registerUser(testUser2);

        User user1=userService.findUserById(1L);
        User user2=userService.findUserById(2L);

        String token1="Bearer "+tokenService.GenerateToken(user1);
        //String token2="Bearer "+tokenService.GenerateToken(user2);

        //matchmakingService.queueMatch(token2);
        //Act
        matchmakingService.queueMatch(token1);

        //Assert
        Assertions.assertTrue(!matchmakingService.matchmaking.isEmpty());
    }

    @Test
    @Order(2)
    public void dequeueMatch(){
        //Arrange
        UserRequestHolder testUser = UserRequestHolder.builder()
                .email("mario-mai@gmx.net")
                .vorname("Mario")
                .nachname("Mai")
                .passwort("12345")
                .build();

        userService.registerUser(testUser);

        UserRequestHolder testUser2 = UserRequestHolder.builder()
                .email("testzweckeio@gmail.com")
                .vorname("Jonas")
                .nachname("Mai")
                .passwort("12345")
                .build();

        userService.registerUser(testUser2);

        User user1=userService.findUserById(1L);
        User user2=userService.findUserById(2L);

        String token1="Bearer "+tokenService.GenerateToken(user1);

        //Act
        matchmakingService.dequeueMatch(token1);


        //Assert
        Assertions.assertTrue(matchmakingService.matchmaking.isEmpty());
    }


    @Test
    @Order(3)
    public void requestMatch(){
        //Arrange
        UserRequestHolder testUser = UserRequestHolder.builder()
                .email("mario-mai@gmx.net")
                .vorname("Mario")
                .nachname("Mai")
                .passwort("12345")
                .build();

        userService.registerUser(testUser);

        UserRequestHolder testUser2 = UserRequestHolder.builder()
                .email("testzweckeio@gmail.com")
                .vorname("Jonas")
                .nachname("Mai")
                .passwort("12345")
                .build();

        userService.registerUser(testUser2);

        User user1=userService.findUserById(1L);
        User user2=userService.findUserById(2L);

        String token1="Bearer "+tokenService.GenerateToken(user1);

        //Act
        matchmakingService.requestMatch(token1, user2.getEmail());


        //Assert
        Assertions.assertTrue(matchRequestRepository.getRequest(user1.getId(),user2.getId()) != null);
    }

    @Test
    @Order(4)
    public void acceptMatch(){
        //Arrange
        UserRequestHolder testUser = UserRequestHolder.builder()
                .email("mario-mai@gmx.net")
                .vorname("Mario")
                .nachname("Mai")
                .passwort("12345")
                .build();

        userService.registerUser(testUser);

        UserRequestHolder testUser2 = UserRequestHolder.builder()
                .email("testzweckeio@gmail.com")
                .vorname("Jonas")
                .nachname("Mai")
                .passwort("12345")
                .build();

        userService.registerUser(testUser2);

        User user1=userService.findUserById(1L);
        User user2=userService.findUserById(2L);

        String token1="Bearer "+tokenService.GenerateToken(user1);

        matchmakingService.requestMatch(token1, user2.getEmail());
        //Act
        matchmakingService.acceptMatchRequest(token1, user2.getId());


        //Assert

    }

    @Test
    @Order(5)
    public void denyMatchRequest(){
        //Arrange
        UserRequestHolder testUser = UserRequestHolder.builder()
                .email("mario-mai@gmx.net")
                .vorname("Mario")
                .nachname("Mai")
                .passwort("12345")
                .build();

        userService.registerUser(testUser);

        UserRequestHolder testUser2 = UserRequestHolder.builder()
                .email("testzweckeio@gmail.com")
                .vorname("Jonas")
                .nachname("Mai")
                .passwort("12345")
                .build();

        userService.registerUser(testUser2);

        User user1=userService.findUserById(1L);
        User user2=userService.findUserById(2L);

        String token1="Bearer "+tokenService.GenerateToken(user1);

        matchmakingService.requestMatch(token1, user2.getEmail());
        //Act
        matchmakingService.denyMatchRequest(token1);


        //Assert
        Assertions.assertTrue(matchRequestRepository.getRequest(user1.getId(),user2.getId()) == null);
    }


}
