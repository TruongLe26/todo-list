package com.leqtr.todolist.controller;

import com.leqtr.todolist.model.Notification;
import com.leqtr.todolist.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/notifications")
    public ModelAndView showNotificationsPage(Model model) {
        List<Notification> notifications = notificationService.getNotifications();
        model.addAttribute("notifications", notifications);
        return new ModelAndView("notifications");
    }

}
