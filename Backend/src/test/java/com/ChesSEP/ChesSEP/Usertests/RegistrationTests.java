package com.ChesSEP.ChesSEP.Usertests;

import java.sql.Date;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import com.ChesSEP.ChesSEP.User.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ChesSEP.ChesSEP.Security.RequestHolder.UserWrapper;
import com.ChesSEP.ChesSEP.User.Role;
import com.ChesSEP.ChesSEP.User.User;

@SpringBootTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@AutoConfigureMockMvc
@TestPropertySource(properties = {
	"spring.jpa.defer-database-initialization=true",
	"spring.jpa.hibernate.ddl.auto=create-drop"
})
public class RegistrationTests {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private MockMvc mockMvc;
    private ObjectMapper objectMapper=new ObjectMapper();

    @Test
    public void Registration_saveUser_returnToken() throws Exception{
        //Arrange
            UserWrapper testuser=UserWrapper.builder()
            .vorname("jonas")
            .nachname("giesen")
            .email("test@mail.com")
            .passwort("foof123")
            .build();

            String jsonRequest=objectMapper.writeValueAsString(testuser);

        //Act
            ResultActions response = mockMvc
                .perform(post("/users/register")
                    .content(jsonRequest)
                    .contentType(MediaType.APPLICATION_JSON));


        //Assert

            Assertions.assertNotNull(response);
            Assertions.assertEquals(1, userService.findAllUsers().size());
    }
}
