package com.ChesSEP.ChesSEP;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.ChesSEP.ChesSEP.User.Role;
import com.ChesSEP.ChesSEP.User.User;
import com.ChesSEP.ChesSEP.User.UserService;

@SpringBootTest
class ChesSepApplicationTests {

	@Autowired
	private UserService userService;

	@Test
	public void UserRepository_getAllUsers(){
		//Arrange
		userService.createUser("Jonas", "Giesen", "jonas@gmail.com", "1234", Role.USER);

		//Act
		User result=userService.getAllUsers().get(0);


		//Assert
		Assertions.assertEquals(result.getVorname(),"Jonas");
		Assertions.assertEquals(result.getNachname(),"Giesen");
		Assertions.assertEquals(result.getEmail(),"jonas@gmail.com");
		Assertions.assertEquals(result.getPassword(),"1234");
		Assertions.assertEquals(result.getRole(),Role.USER);
	}

}
