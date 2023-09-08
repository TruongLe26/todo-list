package com.leqtr.todolistdemo2.controller;

import com.leqtr.todolistdemo2.dto.UserRegistrationDto;
import com.leqtr.todolistdemo2.model.User;
import com.leqtr.todolistdemo2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/registration")
public class UserRegistrationController {

    @Autowired
    private UserService userService;

    @ModelAttribute("user")
    public UserRegistrationDto userRegistrationDto() {
        return new UserRegistrationDto();
    }

    @GetMapping
    public ModelAndView showRegistrationForm() {
        return new ModelAndView("registration");
    }

    @PostMapping
    public ModelAndView registerUserAccount(@ModelAttribute("user") UserRegistrationDto registrationDto) {
        String emailPattern = ".*@gmail\\.com$";
        Pattern pattern = Pattern.compile(emailPattern);
        Matcher matcher = pattern.matcher(registrationDto.getEmail());

        if (!matcher.matches()) {
            ModelAndView modelAndView = new ModelAndView("registration");
            modelAndView.addObject("invalidEmail", true);
            modelAndView.addObject("emailError", "Invalid email, please use a Gmail address");
            return modelAndView;
        }

        User savedUser = userService.save(registrationDto);

        if (savedUser == null) {
            ModelAndView modelAndView = new ModelAndView("registration");
            modelAndView.addObject("emailTaken", true);
            return modelAndView;
        }

        return new ModelAndView("redirect:/registration?success");
    }

}
