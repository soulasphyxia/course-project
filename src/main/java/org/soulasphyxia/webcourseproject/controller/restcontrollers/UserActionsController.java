package org.soulasphyxia.webcourseproject.controller.restcontrollers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.soulasphyxia.webcourseproject.entity.UserAction;
import org.soulasphyxia.webcourseproject.service.UserActionsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/user/logs")
public class UserActionsController {
    private final UserActionsService userActionsService;

    @PostMapping
    public void add(@RequestBody UserAction userAction, HttpServletRequest request) {
        userAction.setUserIp(getRequestIp(request));
        log.info("{}", userAction);
        this.userActionsService.add(userAction);
    }

    private String getRequestIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.isBlank()) {
            return "127.0.0.1";
        }
        return ip;
    }
}
