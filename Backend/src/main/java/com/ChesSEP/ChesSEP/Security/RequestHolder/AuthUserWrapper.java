package com.ChesSEP.ChesSEP.Security.RequestHolder;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AuthUserWrapper {
    private String email;
    private String passwort;
}
