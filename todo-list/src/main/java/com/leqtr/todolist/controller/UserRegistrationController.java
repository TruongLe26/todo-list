package com.leqtr.todolist.controller;

import com.leqtr.todolist.dto.UserRegistrationDto;
import com.leqtr.todolist.model.User;
import com.leqtr.todolist.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

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
    public ModelAndView registerUserAccount(
            @Valid @ModelAttribute("user") UserRegistrationDto registrationDto,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return new ModelAndView("registration");
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
