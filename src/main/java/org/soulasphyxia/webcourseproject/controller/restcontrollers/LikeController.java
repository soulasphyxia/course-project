package org.soulasphyxia.webcourseproject.controller.restcontrollers;

import lombok.RequiredArgsConstructor;
import org.soulasphyxia.webcourseproject.service.VideoService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/user")
@RestController
@RequiredArgsConstructor
public class LikeController {
    private final VideoService videoService;

    @PostMapping("/like/videos/{videoId}")
    public void likeVideo(@PathVariable long videoId) {
        videoService.likeVideo(videoId);
    }

    @PostMapping("/remove-like/videos/{videoId}")
    public void removeLikeVideo(@PathVariable long videoId) {
        videoService.removeLikeVideo(videoId);
    }

    @PostMapping("/dislike/videos/{videoId}")
    public void dislikeVideo(@PathVariable long videoId) {
        videoService.dislikeVideo(videoId);
    }

    @PostMapping("/remove-dislike/videos/{videoId}")
    public void removeDislikeVideo(@PathVariable long videoId) {
        videoService.removeDislikeVideo(videoId);
    }
}
