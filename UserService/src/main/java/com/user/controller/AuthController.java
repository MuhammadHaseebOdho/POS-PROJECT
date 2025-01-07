package com.user.controller;

import com.user.configuration.CustomUserDetailsService;
import com.user.configuration.JwtService;
import com.user.model.User;
import com.user.payload.AuthorizationRequest;
import com.user.payload.AuthorizationResponse;
import com.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    UserService userService;
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtService jwtService;

    @Autowired
    CustomUserDetailsService customUserDetailsService;

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User user){
        System.out.println("Hello");
        User userServiceUser = userService.createUser(user);
        return new ResponseEntity<>(userServiceUser, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthorizationResponse> login(@RequestBody AuthorizationRequest authorizationRequest) throws Exception {
        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authorizationRequest.getEmail(),authorizationRequest.getPassword()));

        }catch (BadCredentialsException e){
            throw new Exception("Incorrect Email or Password");
        }
        final UserDetails userDetails=customUserDetailsService.loadUserByUsername(authorizationRequest.getEmail());
        final String jwt=jwtService.generateToken(userDetails);
        return new ResponseEntity<>(new AuthorizationResponse(jwt),HttpStatus.OK);
    }



}
