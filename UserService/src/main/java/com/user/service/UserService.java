package com.user.service;

import com.user.model.User;
import com.user.payload.UserResponseDto;
import org.springframework.stereotype.Service;


public interface UserService {
    UserResponseDto createUser(User user);

    UserResponseDto getUser(long id);

    UserResponseDto updateUser(long id, User user);
}
