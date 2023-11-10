package com.ChesSEP.ChesSEP.FriendListtests;

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

import com.ChesSEP.ChesSEP.Friendlist.FriendRepository;
import com.ChesSEP.ChesSEP.Friendlist.FriendService;
import com.ChesSEP.ChesSEP.Security.JWT.TokenService;
import com.ChesSEP.ChesSEP.Security.RequestHolder.UserRequestHolder;
import com.ChesSEP.ChesSEP.User.User;
import com.ChesSEP.ChesSEP.User.UserService;

@SpringBootTest
@RunWith(SpringRunner.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@TestPropertySource(properties = {
	"spring.jpa.defer-database-initialization=true",
	"spring.jpa.hibernate.ddl.auto=create-drop",
    "spring.jpa.properties.hibernate.globally_quoted_identifiers=true"
})
public class FriendListtests {

    

    @Autowired
    FriendService friendService;

    @Autowired
    TokenService tokenService;

    @Autowired 
    FriendRepository friendRepository;

    @Autowired
    UserService userService;
    
    @Test
    @Order(1)
    public void FriendService_sendFriendRequest(){
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
        String token2="Bearer "+tokenService.GenerateToken(user2);

        //Act
        friendService.sendFriendRequest(token1, user2.getEmail());

        //Assert
        Assertions.assertTrue(friendRepository.getRequest(user1.getId(),user2.getId()) != null);
    }

    @Test
    @Order(2)
    public void FriendService_acceptFriendRequest(){
        //Arrange
        User user1=userService.findUserById(1L);
        User user2=userService.findUserById(2L);

        String token1="Bearer "+tokenService.GenerateToken(user1);
        String token2="Bearer "+tokenService.GenerateToken(user2);

        //Act
        friendService.acceptFriendRequest(token2, user1.getId());

        //Assert
        Assertions.assertTrue(friendRepository.getFriends(user1.getId(), user2.getId())!=null);
    }

    @Test
    @Order(3)
    public void FriendService_deleteFriend(){
        //Arrange
        User user1=userService.findUserById(1L);
        User user2=userService.findUserById(2L);

        String token1="Bearer "+tokenService.GenerateToken(user1);
        String token2="Bearer "+tokenService.GenerateToken(user2);

        //Act
        friendService.deleteFriend(token2, user1.getId());

        //Assert
        Assertions.assertTrue(friendRepository.getFriends(user1.getId(), user2.getId())==null);
    }
}

