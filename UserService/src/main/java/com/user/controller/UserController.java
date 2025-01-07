package com.user.controller;

import com.user.model.User;
import com.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    UserService userService;

  /*  @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User user){
        User userServiceUser = userService.createUser(user);
        return new ResponseEntity<>(userServiceUser, HttpStatus.CREATED);
    }*/

    @GetMapping("/test")
    public ResponseEntity<String> test(){
        return new ResponseEntity<>("Test",HttpStatus.OK);
    }

}
