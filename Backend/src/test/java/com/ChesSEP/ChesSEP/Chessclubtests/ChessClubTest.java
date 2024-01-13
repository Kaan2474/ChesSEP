package com.ChesSEP.ChesSEP.Chessclubtests;

import com.ChesSEP.ChesSEP.Chat.Chat;
import com.ChesSEP.ChesSEP.Chat.ChatRepository;
import com.ChesSEP.ChesSEP.ChessClub.ChessClub;
import com.ChesSEP.ChesSEP.ChessClub.ChessClubRepository;
import com.ChesSEP.ChesSEP.Security.RequestHolder.AuthUserRequestHolder;
import com.ChesSEP.ChesSEP.User.Role;
import com.ChesSEP.ChesSEP.User.User;
import com.ChesSEP.ChesSEP.User.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
//@RunWith(SpringRunner.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@TestPropertySource(properties = {
        "spring.jpa.defer-database-initialization=true",
        "spring.jpa.hibernate.ddl.auto=create-drop",
        "spring.jpa.properties.hibernate.globally_quoted_identifiers=true"
})
public class ChessClubTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper=new ObjectMapper();


    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private ChessClubRepository chessClubRepository;


    @Autowired
    private UserRepository userRepository;



    @Test
    @Order(1)
    public void chessClubService_join_club_With_User_without_Club() throws Exception {
        //Arrange

        User test1 = User.builder()
                .id(1L)
                .vorname("Mario")
                .nachname("Mai")
                .email("mario@gmail.com")
                .passwort("1111")
                .clubId(null)
                .role(Role.USER)
                .build();
        userRepository.save(test1);

        Chat chatForTestClub = Chat.builder()
                .chatId(1L)
                .chessClubName("Testclub")
                .user(new ArrayList<>())
                .build();
        chatRepository.save(chatForTestClub);

        ChessClub testClub = ChessClub.builder()
                .id(1L)
                .chatId(1L)
                .name("Testclub")
                .build();
        chessClubRepository.save(testClub);

        AuthUserRequestHolder correctTestAuth = new AuthUserRequestHolder("mario@gmail.com", "1111", 1111);
        String correctJsonRequest=objectMapper.writeValueAsString(correctTestAuth);

        MvcResult correctResponse=mockMvc
                .perform(post("/users/twoFactor")
                        .content(correctJsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String JWT=correctResponse.getResponse().getContentAsString();
        JWT="Bearer "+JWT.substring(1,JWT.length()-1);


        //ACT
        
        mockMvc.perform(get("/ChessClub/joinClubV2/Testclub")
                        .header("Authorization", JWT))
                .andReturn();

        //Assert

        Assertions.assertEquals(1L, userRepository.findUserById(test1.getId()).getClubId());

    }

    @Test
    @Order(2)
    public void chessClubService_leave_club() throws Exception {
        //Arrange

        User test1 = User.builder()
                .id(1L)
                .vorname("Mario")
                .nachname("Mai")
                .email("mario@gmail.com")
                .passwort("1111")
                .clubId(null)
                .role(Role.USER)
                .build();
        userRepository.save(test1);

        Chat chatForTestClub = Chat.builder()
                .chatId(1L)
                .chessClubName("Testclub")
                .user(new ArrayList<>())
                .build();
        chatRepository.save(chatForTestClub);

        ChessClub testClub = ChessClub.builder()
                .id(1L)
                .chatId(1L)
                .name("Testclub")
                .build();
        chessClubRepository.save(testClub);

        AuthUserRequestHolder correctTestAuth = new AuthUserRequestHolder("mario@gmail.com", "1111", 1111);
        String correctJsonRequest=objectMapper.writeValueAsString(correctTestAuth);

        MvcResult correctResponse=mockMvc
                .perform(post("/users/twoFactor")
                        .content(correctJsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String JWT=correctResponse.getResponse().getContentAsString();
        JWT="Bearer "+JWT.substring(1,JWT.length()-1);


        //ACT

        mockMvc.perform(get("/ChessClub/leaveClub/1L")
                        .header("Authorization", JWT))
                .andReturn();

        //Assert

        Assertions.assertEquals(null, userRepository.findUserById(test1.getId()).getClubId());


    }

}
