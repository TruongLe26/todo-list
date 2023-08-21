package com.leqtr.todolistdemo2.service;

import com.leqtr.todolistdemo2.dto.UserRegistrationDto;
import com.leqtr.todolistdemo2.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

public interface UserService extends UserDetailsService {

    User save(UserRegistrationDto registrationDto);

}
