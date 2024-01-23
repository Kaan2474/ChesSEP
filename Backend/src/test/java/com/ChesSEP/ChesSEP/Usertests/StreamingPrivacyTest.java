package com.ChesSEP.ChesSEP.Usertests;


import com.ChesSEP.ChesSEP.Chat.Chat;
import com.ChesSEP.ChesSEP.ChessClub.ChessClub;
import com.ChesSEP.ChesSEP.Security.RequestHolder.AuthUserRequestHolder;
import com.ChesSEP.ChesSEP.User.Privacy;
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
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@TestPropertySource(properties = {
        "spring.jpa.defer-database-initialization=true",
        "spring.jpa.hibernate.ddl.auto=create-drop",
        "spring.jpa.properties.hibernate.globally_quoted_identifiers=true"
})
public class StreamingPrivacyTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper=new ObjectMapper();

    @Autowired
    private UserRepository userRepository;

    @Test
    @Order(1)
    public void streamingPrivacy() throws Exception {
        //Arrange

        User test1 = User.builder()
                .id(1L)
                .vorname("Mario")
                .nachname("Mai")
                .email("mario@gmail.com")
                .passwort("1111")
                .clubId(null)
                .role(Role.USER)
                .streaming(Privacy.OEFFENTLICH)
                .build();
        userRepository.save(test1);


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

        mockMvc.perform(get("/users/streamingPrivacy")
                        .header("Authorization", JWT))
                .andReturn();

        //Assert

        Assertions.assertEquals(Privacy.PRIVAT, userRepository.findUserById(test1.getId()).getStreaming());

    }
}
