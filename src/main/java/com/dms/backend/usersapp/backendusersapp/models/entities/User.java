package com.dms.backend.usersapp.backendusersapp.models.entities;

import java.util.List;

import com.dms.backend.usersapp.backendusersapp.models.IUser;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="users")
public class User implements IUser{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 3, max = 12)
    @Column(unique = true)
    private String username;

    @NotBlank
    private String password;

    @Email
    @NotEmpty
    @Column(unique = true)
    private String email; 

    @ManyToMany
    @JoinTable(name = "users_roles", 
               joinColumns = @JoinColumn(name="user_id"), 
               inverseJoinColumns = @JoinColumn(name="role_id"),
               uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "role_id"} )}     
              )
    private List<Role> roles;

    @Transient              //no se mapea a la bbdd
    private boolean admin;

}
