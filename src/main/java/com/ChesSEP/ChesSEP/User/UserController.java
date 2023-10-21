package com.ChesSEP.ChesSEP.User;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
   
    @Autowired
    UserService userService;

    @GetMapping("user/getall")
    public ResponseEntity<Users[]> getallUsers(){
        List<Users> resultList=userService.getAllUsers();
        Users resultArr[]= new Users[resultList.size()];

        for (int i = 0; i < resultArr.length; i++) {
            resultArr[i]=resultList.get(i);
        }

        return new ResponseEntity<Users[]>(resultArr,null,HttpStatus.OK); 
    }
}
