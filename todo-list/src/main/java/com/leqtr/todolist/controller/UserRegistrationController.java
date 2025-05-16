package com.leqtr.todolist.controller;

import com.leqtr.todolist.dto.UserRegistrationDto;
import com.leqtr.todolist.model.User;
import com.leqtr.todolist.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@RequiredArgsConstructor
@RequestMapping("/registration")
public class UserRegistrationController {

    private final UserService userService;

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
