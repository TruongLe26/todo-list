package com.leqtr.todolist.controller;

import com.leqtr.todolist.dto.FormattedNotificationDto;
import com.leqtr.todolist.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/notifications")
    public ModelAndView showNotificationsPage(Model model) {
        String loggingInUser = SecurityContextHolder.getContext().getAuthentication().getName();
        List<FormattedNotificationDto> formattedNotifications = notificationService.formatNotifications(loggingInUser);
        model.addAttribute("notifications", formattedNotifications);
        return new ModelAndView("notifications");
    }
}