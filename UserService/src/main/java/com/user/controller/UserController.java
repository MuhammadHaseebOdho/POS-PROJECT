package com.user.controller;

import com.user.model.User;
import com.user.payload.UserResponseDto;
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

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUser(@PathVariable long id){
        UserResponseDto user = userService.getUser(id);
        return new ResponseEntity<>(user,HttpStatus.FOUND);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable long id,@RequestBody User user){
        UserResponseDto userResponseDto = userService.updateUser(id, user);
        return new ResponseEntity<>(userResponseDto,HttpStatus.OK);
    }

}
