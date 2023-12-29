package com.dms.backend.usersapp.backendusersapp.models.request;

import com.dms.backend.usersapp.backendusersapp.models.IUser;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequest implements IUser{

    @NotBlank
    @Size(min = 3, max = 12)
    private String username;

    @Email
    @NotEmpty
    private String email; 

    private boolean admin;
}
