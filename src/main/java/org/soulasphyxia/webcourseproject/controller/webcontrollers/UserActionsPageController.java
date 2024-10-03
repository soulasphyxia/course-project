package org.soulasphyxia.webcourseproject.controller.webcontrollers;

import lombok.RequiredArgsConstructor;
import org.soulasphyxia.webcourseproject.entity.UserAction;
import org.soulasphyxia.webcourseproject.service.UserActionsService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/admin/user-actions")
@RequiredArgsConstructor
public class UserActionsPageController {
    private final static String PAGE_SIZE = "5";
    private final UserActionsService userActionsService;

    @GetMapping
    public String userActionsPage(Model model,
                                  @RequestParam(value = "date", required = false) String date,
                                  @RequestParam(value = "from", required = false) String from,
                                  @RequestParam(value = "to", required = false) String to,
                                  @RequestParam(defaultValue = "1") int page,
                                  @RequestParam(defaultValue = PAGE_SIZE) int size) {
        Pageable paging = PageRequest.of(page - 1, size);
        Page<UserAction> actions;
        System.out.println(date);
        if (date == null) {
            actions =  userActionsService.getUserActions("","","", paging);
        }else {
            System.out.println("here");
            actions = userActionsService.getUserActions(date, from, to, paging);
        }
        int totalPages = actions.getTotalPages();
        if (totalPages > 0) {
            model.addAttribute("pageNumbers",
                    IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList()));
        }
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("userActions", actions.getContent());
        model.addAttribute("currentPage", actions.getNumber() + 1);
        model.addAttribute("date", date);
        model.addAttribute("from", from);
        model.addAttribute("to", to);
        return "admin/user-actions";
    }
}
