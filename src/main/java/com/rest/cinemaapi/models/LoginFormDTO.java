package com.rest.cinemaapi.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginFormDTO {
    private String login;

    private String password;

    public LoginFormDTO() {
        
    }

    public LoginFormDTO(String login, String password) {
        this.login = login;
        this.password = password;
    }
}
