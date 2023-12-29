package com.dms.backend.usersapp.backendusersapp.models.dto.mapper;

import com.dms.backend.usersapp.backendusersapp.models.dto.UserDto;
import com.dms.backend.usersapp.backendusersapp.models.entities.User;

public class DtoMapperUser {
    
    private User user;

    private DtoMapperUser(){    
    }

    public static DtoMapperUser builder(){
        return new DtoMapperUser(); 
    }

    public DtoMapperUser setUser(User user) {
        this.user = user;
        return this;
    }

    public UserDto build(){     
        if(user == null){
            throw new RuntimeException("We need entity user");
        }
        boolean isAdmin = user.getRoles().stream().anyMatch(rol -> "ROLE_ADMIN".equals(rol.getName()));
        UserDto userDto = new UserDto(this.user.getId(), this.user.getUsername(), this.user.getEmail(), isAdmin);
        return userDto;
    }
    
}
