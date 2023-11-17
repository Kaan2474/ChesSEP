package com.ChesSEP.ChesSEP.User;

public class EmailValidator {
    
    public boolean isEmailValid(String email){

        return email.matches("(\\w|.)+@(\\w)+.(\\w)+");
    }
}
