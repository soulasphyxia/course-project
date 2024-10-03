package org.soulasphyxia.webcourseproject.controller.webcontrollers;
import lombok.RequiredArgsConstructor;
import org.soulasphyxia.webcourseproject.entity.dto.VideoDto;
import org.soulasphyxia.webcourseproject.service.VideoService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user/videos/{videoId}")
public class VideoModelController {
    private final VideoService videoService;

    @ModelAttribute("video")
    public VideoDto video(@PathVariable("videoId") Long videoId) {
        return this.videoService.getVideoById(videoId);
    }

    @GetMapping()
    public String getVideo() {
        return "user/video";
    }
}