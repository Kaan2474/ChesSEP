package com.ChesSEP.ChesSEP.User;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository=userRepository;
    }

    public boolean createUser(String vorname, String nachname,String email, String password,Role role){
        try{
            Users user=new Users(vorname,nachname,email,password,role);
            userRepository.save(user);
            return true;

        }catch(Exception e){
            System.out.println("User konnte nicht erstellt werden: "+e);
            return false;
        }
    }

    public List<Users> getAllUsers(){
        return userRepository.findAll();
    }
}
