package com.ChesSEP.ChesSEP.Security.RequestHolder;

import java.sql.Date;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserWrapper {
    private Long id;
    private String vorname;
    private String nachname;
    private String email;
    private String passwort;
    private Date geburtsdatum;
}
