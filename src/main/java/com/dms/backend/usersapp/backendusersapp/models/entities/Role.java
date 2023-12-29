package com.dms.backend.usersapp.backendusersapp.models.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "roles")
public class Role {

   public Role(String name) {
      this.name = name;
   } 

   public Role() {
   }

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY) 
   private Long id;

   @Column(unique = true)
   private String name;

}
