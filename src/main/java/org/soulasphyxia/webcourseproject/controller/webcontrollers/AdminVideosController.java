package org.soulasphyxia.webcourseproject.controller.webcontrollers;

import lombok.RequiredArgsConstructor;
import org.soulasphyxia.webcourseproject.entity.dto.UpdateVideoDto;
import org.soulasphyxia.webcourseproject.entity.dto.VideoDto;
import org.soulasphyxia.webcourseproject.service.TagService;
import org.soulasphyxia.webcourseproject.service.VideoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RequestMapping("/admin/videos")
@Controller
@RequiredArgsConstructor
public class AdminVideosController {
    private final static String DEFAULT_PAGE_SIZE = "3";
    private final VideoService videoService;
    private final TagService tagService;

    @GetMapping
    public String getVideos(Model model,
                            @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int size,
                            @RequestParam(defaultValue = "1") int page) {
        Pageable paging = PageRequest.of(page - 1, size);
        Page<VideoDto> videos = videoService.getVideos(paging);
        addAttributesToModel(model, videos);
        return "/admin/videos";
    }

    @GetMapping("/search")
    public String getVideosByTitle(Model model,
                                   @RequestParam String title,
                                   @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int size,
                                   @RequestParam(defaultValue = "1") int page) {
        Pageable paging = PageRequest.of(page - 1, size);
        Page<VideoDto> videos = videoService.getVideosByTitle(title, paging);
        addAttributesToModel(model, videos);
        return "/admin/videos";
    }

    @GetMapping("/edit/{videoId}")
    public String getEditPage(Model model, @PathVariable Long videoId) {
        model.addAttribute("video", videoService.getVideoById(videoId));
        model.addAttribute("tags", tagService.findAll());
        return "/admin/edit";
    }

    @PostMapping(value = "/edit/{videoId}")
    public String editVideo(@PathVariable Long videoId,
                            UpdateVideoDto updateVideoDto) {
        videoService.editVideo(videoId, updateVideoDto);
        return "redirect:/admin/videos";
    }

    @PostMapping("/delete/{videoId}")
    public String deleteVideo(@PathVariable Long videoId) {
        videoService.deleteVideoById(videoId);
        return "redirect:/admin/videos";
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
