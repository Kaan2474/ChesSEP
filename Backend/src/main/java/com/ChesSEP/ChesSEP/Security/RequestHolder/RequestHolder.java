package com.ChesSEP.ChesSEP.Security.RequestHolder;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class RequestHolder <T> {
    boolean status;
    T Object;
}
