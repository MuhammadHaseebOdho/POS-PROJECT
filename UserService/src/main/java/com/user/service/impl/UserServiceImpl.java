package com.user.service.impl;

import com.user.model.User;
import com.user.payload.UserResponseDto;
import com.user.repository.UserRepository;
import com.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;


    @Override
    public UserResponseDto createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User save = userRepository.save(user);
        return new UserResponseDto(save.getUserName(), save.getEmail(), save.getRole());
    }

    @Override
    public UserResponseDto getUser(long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new UserResponseDto(user.getUserName(), user.getEmail(), user.getRole());
    }

    @Override
    public UserResponseDto updateUser(long id, User user) {
        userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        user.setId(id);
        User save = userRepository.save(user);
        return new UserResponseDto(save.getUserName(), save.getEmail(), save.getRole());
    }
}
