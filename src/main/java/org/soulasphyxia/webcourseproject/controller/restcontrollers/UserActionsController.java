package org.soulasphyxia.webcourseproject.controller.restcontrollers;

import lombok.RequiredArgsConstructor;
import org.soulasphyxia.webcourseproject.entity.UserAction;
import org.soulasphyxia.webcourseproject.service.UserActionsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user/logs")
public class UserActionsController {
    private final UserActionsService userActionsService;

    @PostMapping
    public void add(@RequestBody UserAction userAction) {
        this.userActionsService.add(userAction);
    }

}
