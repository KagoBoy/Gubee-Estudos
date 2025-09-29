package com.example.first_spring_boot.resources;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.first_spring_boot.entities.User;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping(value = "/users")
public class UserResource {


    @GetMapping
    public ResponseEntity<User> findAll(){
        User u = new User(1L, "Marcos", "marcos@gmail.com", "999999", "12345678");
        return ResponseEntity.ok().body(u);
    }
}
