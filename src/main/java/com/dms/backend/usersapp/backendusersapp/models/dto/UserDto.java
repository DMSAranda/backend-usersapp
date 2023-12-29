package com.dms.backend.usersapp.backendusersapp.models.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto{

    private Long id;

    private String username;

    private String email;

    private boolean admin;

    public UserDto() {
    }

    public UserDto(Long id, String username, String email, boolean admin) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.admin = admin;
    }

}