package org.soulasphyxia.webcourseproject.controller.webcontrollers;

import lombok.RequiredArgsConstructor;
import org.soulasphyxia.webcourseproject.entity.dto.VideoDto;
import org.soulasphyxia.webcourseproject.service.TagService;
import org.soulasphyxia.webcourseproject.service.VideoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminPanelController {
    private final VideoService videoService;
    private final TagService tagService;

    @GetMapping()
    public String getAdminPage() {
        return "/admin/admin";
    }

    @GetMapping("/upload")
    public String getUploadPage(Model model) {
        model.addAttribute("tags", tagService.findAll());
        return "/admin/upload";
    }

    @PostMapping("/upload")
    public String createVideo(VideoDto videoDto,
                              @RequestParam(name = "file") MultipartFile file) {
        videoService.uploadVideo(videoDto,file);
        return "redirect:/admin/videos";
    }

    @GetMapping("/ratings")
    public String getRatings(Model model,
                             @RequestParam(defaultValue = "10") int size,
                             @RequestParam(defaultValue = "1") int page) {
        Pageable paging = PageRequest.of(page - 1, size);
        Page<VideoDto> videos = videoService.getTopVideosByLikes(paging);
        addAttributesToModel(model, videos);
        return "/admin/ratings";
    }

    private void addAttributesToModel(Model model, Page<VideoDto> videos) {
        model.addAttribute("videos", videos.getContent());
        int totalPages = videos.getTotalPages();
        if (totalPages > 0) {
            model.addAttribute("pageNumbers",
                    IntStream.rangeClosed(1, totalPages)
                            .boxed()
                            .collect(Collectors.toList()));
        }
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("videos", videos.getContent());
        model.addAttribute("currentPage", videos.getNumber() + 1);
    }
}
