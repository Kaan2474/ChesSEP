package com.ChesSEP.ChesSEP.Security.RequestHolder;

import java.sql.Date;

import com.ChesSEP.ChesSEP.User.Privacy;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class UserRequestHolder {
    private Long id;
    private String vorname;
    private String nachname;
    private String email;
    private String passwort;
    private Date geburtsdatum;
    private int elo;
    private byte[] profilbild;
    private Privacy privacy;
}
