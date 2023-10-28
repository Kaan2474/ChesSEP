package com.ChesSEP.ChesSEP.Usertests;

import com.ChesSEP.ChesSEP.User.Role;
import com.ChesSEP.ChesSEP.User.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class foof {
    
    static ObjectMapper objectMapper=new ObjectMapper();
    public static void main(String[] args) throws JsonProcessingException {
        User testuser=User.builder()
            .vorname("jonas")
            .nachname("giesen")
            .email("test@mail.com")
            .passwort("foof123")
            .role(Role.USER)
            .build();

            String jsonRequest=objectMapper.writeValueAsString(testuser);

            System.out.println(jsonRequest);
    }

}


    

