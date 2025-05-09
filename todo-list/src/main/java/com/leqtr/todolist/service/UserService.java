package com.leqtr.todolist.service;

import com.leqtr.todolist.dto.UserRegistrationDto;
import com.leqtr.todolist.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    User save(UserRegistrationDto registrationDto);
    void processOAuthPostLogin(String username);
    User findByEmail(String email);

}
