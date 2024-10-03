package org.soulasphyxia.webcourseproject.controller.restcontrollers;

import lombok.RequiredArgsConstructor;
import org.soulasphyxia.webcourseproject.service.S3Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/s3")
@RequiredArgsConstructor
public class S3Controller {
    private final S3Service s3Service;

    @GetMapping("/videos")
    public List<String> getVideos() {
        return s3Service.getVideos();
    }

    @DeleteMapping("/delete")
    public String deleteVideos() {
        return s3Service.deleteVideos();
    }
}
