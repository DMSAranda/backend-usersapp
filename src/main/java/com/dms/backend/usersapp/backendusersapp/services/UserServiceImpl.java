package com.dms.backend.usersapp.backendusersapp.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dms.backend.usersapp.backendusersapp.models.IUser;
import com.dms.backend.usersapp.backendusersapp.models.dto.UserDto;
import com.dms.backend.usersapp.backendusersapp.models.dto.mapper.DtoMapperUser;
import com.dms.backend.usersapp.backendusersapp.models.entities.Role;
import com.dms.backend.usersapp.backendusersapp.models.entities.User;
import com.dms.backend.usersapp.backendusersapp.models.request.UserRequest;
import com.dms.backend.usersapp.backendusersapp.repositories.RoleRepository;
import com.dms.backend.usersapp.backendusersapp.repositories.UserRepository;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository repository;

    @Autowired
    private RoleRepository repository2;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> findAll() {   

        List<User> userList = (List<User>) repository.findAll();

        List<UserDto> userDtoList = userList.stream()
                                            .map(user -> DtoMapperUser.builder().setUser(user).build())
                                            .collect(Collectors.toList());

        return userDtoList; 
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserDto> findByID(Long id) {
        Optional<User> user = repository.findById(id);
        if(user.isPresent()){
            return Optional.of(DtoMapperUser.builder().setUser(user.orElseThrow()).build());
        } 
        return Optional.empty();
    }

    @Override
    @Transactional
    public void remove(Long id) {
        repository.deleteById(id);
    }

    @Override
    @Transactional
    public UserDto save(User user) {
        String passwordBCrypt = passwordEncoder.encode(user.getPassword());
        user.setPassword(passwordBCrypt);

        user.setRoles(getRoles(user));   

        return DtoMapperUser.builder().setUser(repository.save(user)).build();
    }

    @Override
    @Transactional
    public Optional<UserDto> update(UserRequest userRequest, Long id) {
        Optional<User> userOptional = repository.findById(id);
        User user = null;
        if (userOptional.isPresent()){

            User userDB = userOptional.orElseThrow();
            userDB.setRoles(getRoles(userRequest));
            userDB.setUsername(userRequest.getUsername());
            userDB.setEmail(userRequest.getEmail());
            user = repository.save(userDB);
        }
        return Optional.ofNullable(DtoMapperUser.builder().setUser(user).build());            
    }

    private List<Role> getRoles(IUser user){
        
        Optional<Role> op = repository2.findByName("ROLE_USER");
        List<Role> roles = new ArrayList<>();
        if (op.isPresent()){
           roles.add(op.orElseThrow()); 
        }
        if(user.isAdmin()){
            Optional<Role> op2 = repository2.findByName("ROLE_ADMIN");
            if(op2.isPresent()){
                roles.add(op2.orElseThrow());
            }
        }
        return roles;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserDto> findAll(Pageable pageable){
        
        Page<User> usersPage = repository.findAll(pageable);

        return usersPage.map(user -> DtoMapperUser.builder().setUser(user).build());
    }; 
 
}
