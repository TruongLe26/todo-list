package com.leqtr.todolistdemo2.controller;

import com.leqtr.todolistdemo2.model.Notification;
import com.leqtr.todolistdemo2.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/notifications")
    public ModelAndView showNotificationsPage(Model model) {
        List<Notification> notifications = notificationService.getNotifications();
        model.addAttribute("notifications", notifications);
        return new ModelAndView("notifications");
    }

}
