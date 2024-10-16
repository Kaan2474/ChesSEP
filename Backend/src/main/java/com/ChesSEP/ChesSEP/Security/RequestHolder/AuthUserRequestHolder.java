package com.ChesSEP.ChesSEP.Security.RequestHolder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class AuthUserRequestHolder {
    private String email;
    private String passwort;
    private int twoFactor;
}
