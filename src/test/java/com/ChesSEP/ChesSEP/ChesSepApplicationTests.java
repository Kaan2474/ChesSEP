package com.ChesSEP.ChesSEP;

import org.h2.engine.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import com.ChesSEP.ChesSEP.User.Role;
import com.ChesSEP.ChesSEP.User.UserRepository;
import com.ChesSEP.ChesSEP.User.Users;
import com.ChesSEP.ChesSEP.User.UserService;

@SpringBootTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@TestPropertySource(properties = {
	"spring.jpa.defer-database-initialization=true",
	"spring.jpa.hibernate.ddl.auto=create-drop"
})
class ChesSepApplicationTests {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserService userService;

	@Test
	public void UserRepository_saveUser_returnsUser(){
		//Arrange
		Users users =new Users("Jonas","Giesen","jonas@gmail.com","1234",Role.USER);
		userRepository.save(users);

		//Act
		Users result=userService.getAllUsers().get(0);


		//Assert
		Assertions.assertEquals(result.getVorname(),"Jonas");
		Assertions.assertEquals(result.getNachname(),"Giesen");
		Assertions.assertEquals(result.getEmail(),"jonas@gmail.com");
		Assertions.assertEquals(result.getPassword(),"1234");
		Assertions.assertEquals(result.getRole(),Role.USER);
	}

}
