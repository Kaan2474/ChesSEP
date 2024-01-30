/*package com.ChesSEP.ChesSEP.Usertests;

import java.sql.Date;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ChesSEP.ChesSEP.Security.RequestHolder.UserRequestHolder;
import com.ChesSEP.ChesSEP.Security.RequestHolder.AuthUserRequestHolder;


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
public class RegistrationTests {
    
    //@Autowired
    //private UserService userService;
    
    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper=new ObjectMapper();

    @Test
    @Order(1)
    public void Registration_saveUser() throws Exception{
        //Arrange
            UserRequestHolder testuser=UserRequestHolder.builder()
                .vorname("jonas")
                .nachname("giesen")
                .email("jonas.giesen2004@gmail.com")
                .passwort("foof123")
                .geburtsdatum(new Date(System.currentTimeMillis()))
                .build();

            String jsonRequest=objectMapper.writeValueAsString(testuser);

        //Act
            MvcResult response = mockMvc
                .perform(post("/users/register")
                    .content(jsonRequest)
                    .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

            MvcResult duplicateEmailresponse = mockMvc
                .perform(post("/users/register")
                    .content(jsonRequest)
                    .contentType(MediaType.APPLICATION_JSON))
                .andReturn();;


        //Assert
            Assertions.assertNotNull(response.getResponse().getContentAsString());
            Assertions.assertEquals("\"Die Email existiert bereits oder ist falsch!\"",duplicateEmailresponse.getResponse().getContentAsString());
            //Assertions.assertEquals(1, userService.findAllUsers().size());
    }

    @Test
    @Order(2)
    public void Authentication_loginUser() throws Exception{
        //Arrange
            AuthUserRequestHolder wrongPasswordTestAuth = new AuthUserRequestHolder("jonas.giesen2004@gmail.com", "foof1337", 999999);
            String wrongPasswordJsonRequest=objectMapper.writeValueAsString(wrongPasswordTestAuth);

            AuthUserRequestHolder wrongEmailTestAuth = new AuthUserRequestHolder("test1@mail.com", "foof123", 999999);
            String wrongEmailJsonRequest=objectMapper.writeValueAsString(wrongEmailTestAuth);

            AuthUserRequestHolder correctTestAuth = new AuthUserRequestHolder("jonas.giesen2004@gmail.com", "foof123", 999999);
            String correctJsonRequest=objectMapper.writeValueAsString(correctTestAuth);

        //Act
            MvcResult wrongPasswordResponse=mockMvc
                .perform(post("/users/authenticate")
                    .content(wrongPasswordJsonRequest)
                    .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

            MvcResult wrongEmailResponse=mockMvc
                .perform(post("/users/authenticate")
                    .content(wrongEmailJsonRequest)
                    .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

            MvcResult correctResponse=mockMvc
                .perform(post("/users/authenticate")
                    .content(correctJsonRequest)
                    .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        //Assert
            Assertions.assertTrue(wrongPasswordResponse.getResponse().getContentAsString().startsWith("\"Fehler beim versenden der Email oder erstellen des Codes:"));
            Assertions.assertTrue(wrongEmailResponse.getResponse().getContentAsString().startsWith("\"Fehler beim versenden der Email oder erstellen des Codes:"));
            Assertions.assertTrue(!correctResponse.getResponse().getContentAsString().startsWith("\"Fehler beim versenden der Email oder erstellen des Codes:"));
    }

    @Test
    @Order(3)
    public void Authentication_authenticateUser_returnFromSecuredEndpoint() throws Exception{
        //Arrange
            AuthUserRequestHolder correctTestAuth = new AuthUserRequestHolder("jonas.giesen2004@gmail.com", "foof123", 1111);
            String correctJsonRequest=objectMapper.writeValueAsString(correctTestAuth);

            MvcResult correctResponse=mockMvc
                .perform(post("/users/twoFactor")
                    .content(correctJsonRequest)
                    .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

            String JWT=correctResponse.getResponse().getContentAsString();
            JWT="Bearer "+JWT.substring(1,JWT.length()-1);
        //Act
            MvcResult nonAuthentcatedResponse=mockMvc
                .perform(get("/users/hello"))
                .andReturn();  

            MvcResult authenticatedResponse=mockMvc
                .perform(get("/users/hello")
                    .header("Authorization", JWT))
                .andReturn();  

        //Assert

            Assertions.assertEquals(403,nonAuthentcatedResponse.getResponse().getStatus());
            Assertions.assertEquals(200,authenticatedResponse.getResponse().getStatus());
            Assertions.assertEquals("Hello World!", authenticatedResponse.getResponse().getContentAsString());
    }
}

 */
