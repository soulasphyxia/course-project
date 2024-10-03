package org.soulasphyxia.webcourseproject.controller.webcontrollers;

import lombok.RequiredArgsConstructor;
import org.soulasphyxia.webcourseproject.entity.Video;
import org.soulasphyxia.webcourseproject.entity.dto.VideoDto;
import org.soulasphyxia.webcourseproject.service.VideoService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminPanelController {
    private final static String DEFAULT_PAGE_SIZE = "3";

    private final VideoService videoService;

    @GetMapping("/videos")
    public String getVideos(Model model,
                            @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int size,
                            @RequestParam(defaultValue = "1") int page) {
        Pageable paging = PageRequest.of(page - 1, size);
        Page<VideoDto> videos = videoService.getVideos(paging, null);
        addAttributesToModel(model, videos);
        return "/admin/videos";
    }

    @GetMapping()
    public String getAdminPage() {
        return "/admin/admin";
    }

    @GetMapping("/upload")
    public String getUploadPage(Model model) {
        return "/admin/upload";
    }

    @PostMapping("/upload")
    public String createVideo(Model model,
                              VideoDto videoDto,
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

    @PostMapping("/delete/{videoId}")
    public String deleteVideo(@PathVariable Long videoId) {
        videoService.deleteVideoById(videoId);
        return "redirect:/admin/videos";
    }
}
