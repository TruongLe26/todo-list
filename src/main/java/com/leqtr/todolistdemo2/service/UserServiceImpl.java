package com.leqtr.todolistdemo2.service;

import com.leqtr.todolistdemo2.config.SecurityUtil;
import com.leqtr.todolistdemo2.dto.UserRegistrationDto;
import com.leqtr.todolistdemo2.model.Provider;
import com.leqtr.todolistdemo2.model.Role;
import com.leqtr.todolistdemo2.model.TodoItem;
import com.leqtr.todolistdemo2.model.User;
import com.leqtr.todolistdemo2.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SecurityUtil securityUtil;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public User save(UserRegistrationDto registrationDto) {
        if (userRepository.findByEmail(registrationDto.getEmail()) != null) {
            return null;
        }
        User user = new User(
                registrationDto.getFirstName(),
                registrationDto.getLastName(),
                registrationDto.getEmail(),
                passwordEncoder.encode(registrationDto.getPassword()),
                List.of(new Role("ROLE_USER"))
        );
        return userRepository.save(user);
    }

    @Override
    public void processOAuthPostLogin(String username) {
        User existUser = userRepository.findByEmail(username);

        if (existUser == null) {
            User newUser = new User();
            newUser.setEmail(username);
            newUser.setFirstName(securityUtil.getFirstNameOAuth());
            newUser.setLastName(securityUtil.getLastNameOAuth());
            newUser.setProvider(Provider.GOOGLE);

            userRepository.save(newUser);
        }
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("Invalid username or password");
        }
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                mapRolesToAuthorities(user.getRoles())
        );
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }
}
