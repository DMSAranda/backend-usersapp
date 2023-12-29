package com.dms.backend.usersapp.backendusersapp.repositories;

import org.springframework.data.repository.CrudRepository;

import com.dms.backend.usersapp.backendusersapp.models.entities.Role;
import java.util.Optional;


public interface RoleRepository extends CrudRepository<Role, Long>{

    Optional<Role> findByName(String name);
}
