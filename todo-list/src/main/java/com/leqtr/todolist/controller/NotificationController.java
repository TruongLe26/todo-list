package com.leqtr.todolist.controller;

import com.leqtr.todolist.dto.NotificationDto;
import com.leqtr.todolist.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/notifications")
    public ModelAndView showNotificationsPage(Model model) {
        String loggingInUser = SecurityContextHolder.getContext().getAuthentication().getName();
        List<NotificationDto> notifications = notificationService.getNotifications(loggingInUser);
        List<Map<String, Object>> formattedNotifications = notifications.stream()
                .map(n -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("content", n.getContent());
                    map.put("timestamp", n.getTimestamp());
                    map.put("formattedDate", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                            .withZone(ZoneId.systemDefault())
                            .format(Instant.parse(n.getTimestamp())));
                    return map;
                })
                .toList();
        model.addAttribute("notifications", formattedNotifications);
        return new ModelAndView("notifications");
    }

}
