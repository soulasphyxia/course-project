package org.soulasphyxia.webcourseproject.controller.webcontrollers;
import lombok.RequiredArgsConstructor;
import org.soulasphyxia.webcourseproject.entity.dto.VideoDto;
import org.soulasphyxia.webcourseproject.service.VideoService;
import org.soulasphyxia.webcourseproject.utils.AuthenticationSystem;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    public String getVideo(Model model) {
        model.addAttribute("isLogged", AuthenticationSystem.isLogged());
        return "user/video";
    }
}