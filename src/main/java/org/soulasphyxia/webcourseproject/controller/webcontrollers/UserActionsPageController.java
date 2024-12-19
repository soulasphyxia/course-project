package org.soulasphyxia.webcourseproject.controller.webcontrollers;

import lombok.RequiredArgsConstructor;
import org.soulasphyxia.webcourseproject.entity.UserAction;
import org.soulasphyxia.webcourseproject.entity.dto.UserActionsDateDto;
import org.soulasphyxia.webcourseproject.service.UserActionsService;
import org.soulasphyxia.webcourseproject.user_actions.LogType;
import org.soulasphyxia.webcourseproject.utils.ResourceObject;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.io.InputStream;
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
                                  UserActionsDateDto dateDto,
                                  @RequestParam(defaultValue = "1") int page,
                                  @RequestParam(defaultValue = PAGE_SIZE) int size) {
        Pageable paging = PageRequest.of(page - 1, size, Sort.by("dateTime").descending());
        Page<UserAction> actions = userActionsService.getUserActions(dateDto, paging);
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
        model.addAttribute("userActionDateDto", dateDto);
        return "admin/user-actions";
    }

    @GetMapping("/download")
    public ResponseEntity<Resource> download(@RequestParam LogType format,
                                             UserActionsDateDto dateDto) {
        ResourceObject object = userActionsService.download(dateDto, format);
        try (InputStream inputStream = object.stream()) {
            ByteArrayResource resource = new ByteArrayResource(inputStream.readAllBytes());
            return ResponseEntity.ok()
                    .contentLength(resource.contentLength())
                    .contentType(object.contentType())
                    .header("Content-Disposition", "attachment; filename=%s%s".formatted(object.name(), format.extension))
                    .body(resource);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
