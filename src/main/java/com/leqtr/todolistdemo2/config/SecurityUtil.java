package com.leqtr.todolistdemo2.config;

import com.leqtr.todolistdemo2.model.User;
import com.leqtr.todolistdemo2.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class SecurityUtil {

    @Autowired
    private UserRepository userRepository;

    public String getSessionUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof OAuth2AuthenticationToken) {
            OAuth2User user = (OAuth2User) authentication.getPrincipal();
            Map<String, Object> attributes = user.getAttributes();

            for (Map.Entry<String, Object> entry : attributes.entrySet()) {
                if (entry.getKey().equals("email")) {
                    return (String) entry.getValue();
                }
            }
        }
        return authentication.getName();
    }

    public String getInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof OAuth2AuthenticationToken) {
            return authentication.getName();
        }

        User user = userRepository.findByEmail(authentication.getName());
        return user.getFirstName() + " " + user.getLastName();
    }

    public String getFirstNameOAuth() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof OAuth2AuthenticationToken) {
            OAuth2User user = (OAuth2User) authentication.getPrincipal();
            Map<String, Object> attributes = user.getAttributes();

            for (Map.Entry<String, Object> entry : attributes.entrySet()) {
                if (entry.getKey().equals("given_name")) {
                    return (String) entry.getValue();
                }
            }
        }
        return null;
    }

    public String getLastNameOAuth() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof OAuth2AuthenticationToken) {
            OAuth2User user = (OAuth2User) authentication.getPrincipal();
            Map<String, Object> attributes = user.getAttributes();

            for (Map.Entry<String, Object> entry : attributes.entrySet()) {
                if (entry.getKey().equals("family_name")) {
                    return (String) entry.getValue();
                }
            }
        }
        return null;
    }

}
