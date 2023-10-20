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
            User user=new User(vorname,nachname,email,password,role);
            userRepository.save(user);
            return true;

        }catch(Exception e){
            System.out.println("User konnte nicht erstellt werden: "+e);
            return false;
        }
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }
}
