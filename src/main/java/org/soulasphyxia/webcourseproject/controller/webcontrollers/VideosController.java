package org.soulasphyxia.webcourseproject.controller.webcontrollers;

import lombok.RequiredArgsConstructor;
import org.soulasphyxia.webcourseproject.entity.Video;
import org.soulasphyxia.webcourseproject.entity.VideoVisibility;
import org.soulasphyxia.webcourseproject.entity.dto.VideoDto;
import org.soulasphyxia.webcourseproject.service.VideoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user/videos")
public class VideosController {
    private final static String DEFAULT_PAGE_SIZE = "6";
    private final VideoService videoService;

    @GetMapping()
    public String getVideos(Model model,
                            @RequestParam(required = false) String pattern,
                            @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int size,
                            @RequestParam(defaultValue = "1") int page) {
        Predicate<Video> filter = video -> video.getVisibility().equals(VideoVisibility.PUBLIC);
        Pageable paging = PageRequest.of(page - 1, size);
        Page<VideoDto> videos = this.videoService.getVideos(paging, filter);
        addAttributesToModel(model, videos);
        model.addAttribute("pattern", pattern);
        System.out.println(videos.getPageable());
        return "user/videos";
    }

    @GetMapping("/filter")
    public String filterVideos(Model model,
                               @RequestParam(required = false) String pattern,
                               @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int size,
                               @RequestParam(defaultValue = "1") int page) {
        Pageable paging = PageRequest.of(page - 1, size);
        Page<VideoDto> videos = this.videoService.getFilteredVideos(pattern, paging);
        addAttributesToModel(model, videos);
        model.addAttribute("pattern", pattern);
        return "user/filtered";
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
