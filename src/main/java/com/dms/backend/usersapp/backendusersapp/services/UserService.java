package com.dms.backend.usersapp.backendusersapp.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.dms.backend.usersapp.backendusersapp.models.dto.UserDto;
import com.dms.backend.usersapp.backendusersapp.models.entities.User;
import com.dms.backend.usersapp.backendusersapp.models.request.UserRequest;

public interface UserService {

    List<UserDto> findAll();

    Optional<UserDto> findByID(Long id);

    UserDto save(User user);

    Optional<UserDto> update(UserRequest userRequest, Long id);

    void remove(Long id);

    Page<UserDto> findAll(Pageable pageable); 
}
