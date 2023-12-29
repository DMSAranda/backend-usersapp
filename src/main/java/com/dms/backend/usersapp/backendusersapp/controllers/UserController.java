package com.dms.backend.usersapp.backendusersapp.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dms.backend.usersapp.backendusersapp.models.dto.UserDto;
import com.dms.backend.usersapp.backendusersapp.models.entities.User;
import com.dms.backend.usersapp.backendusersapp.models.request.UserRequest;
import com.dms.backend.usersapp.backendusersapp.services.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
@CrossOrigin(originPatterns = "*")
public class UserController {

    @Autowired
    private UserService service;

    @GetMapping
    public List<UserDto> list(){

        return service.findAll();
    };

    @GetMapping("/page/{page}")
    public Page<UserDto> list(@PathVariable Integer page){
        
        Pageable pageable = PageRequest.of(page, 4);

        return service.findAll(pageable);
    };

 /* 
     @GetMapping("/{id}")
    public User show(@PathVariable Long id){
        return service.findByID(id).orElseThrow();
    };
    DEBAJO LA OPCION CON RESPONSE ENTITY
 */  
    @GetMapping("/{id}")
    public ResponseEntity<?> show(@PathVariable Long id){
        
        Optional<UserDto> userOptional = service.findByID(id);
        if (userOptional.isPresent()){
            return ResponseEntity.ok(userOptional.orElseThrow());
        }else{
            return ResponseEntity.notFound().build();
        }      
    };    

/*
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@RequestBody User user){
        return service.save(user);
    };
    DEBAJO LA OPCION CON RESPONSE ENTITY
 */
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody User user, BindingResult result){
        if(result.hasErrors()){
          return validation(result);      
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(user));
    };

/*
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@RequestBody User user,@PathVariable Long id){
        
        Optional<User> userOptional = service.findByID(id);
        if (userOptional.isPresent()){
            User userDB = userOptional.orElseThrow();
            userDB.setUsername(user.getUsername());
            userDB.setPass(user.getPass());
            userDB.setEmail(user.getEmail());
            return ResponseEntity.status(HttpStatus.CREATED).body(service.save(userDB));
        }else{
            return ResponseEntity.notFound().build();
        }    
    };
    DEBAJO LA OPCION SEPARANDO LA LOGICA EN EL SERVICIO
 */
    
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@Valid @RequestBody UserRequest userRequest, BindingResult result, @PathVariable Long id){
        if(result.hasErrors()){
            return validation(result);    
        } 
        Optional<UserDto> userOptional = service.update(userRequest, id);
        if (userOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.CREATED).body(userOptional.orElseThrow());
        }    
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> remove(@PathVariable Long id){
        Optional<UserDto> userOptional = service.findByID(id);
        if (userOptional.isPresent()){
            service.remove(id);
            return ResponseEntity.noContent().build();  //204
        }else{
            return ResponseEntity.notFound().build();
        }
    }


    private ResponseEntity<?> validation(BindingResult result){
        Map<String, String> errors = new HashMap<>();
        result.getFieldErrors().forEach(err ->{
            errors.put(err.getField(), "The field " + err.getField() + " " + err.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errors);

    }
}
