package org.soulasphyxia.webcourseproject.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.bytedeco.javacv.FrameGrabber;
import org.soulasphyxia.webcourseproject.entity.Video;
import org.soulasphyxia.webcourseproject.entity.VideoVisibility;
import org.soulasphyxia.webcourseproject.entity.dto.VideoDto;
import org.soulasphyxia.webcourseproject.mapper.VideoMapper;
import org.soulasphyxia.webcourseproject.repository.VideoRepository;
import org.soulasphyxia.webcourseproject.utils.PageUtils;
import org.soulasphyxia.webcourseproject.utils.ThumbnailTaker;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Transactional
public class VideoService {
    private final VideoRepository videoRepository;
    private final S3Service s3Service;
    private final VideoMapper videoMapper;
    private final ThumbnailTaker thumbnailTaker;

    public Page<VideoDto> getVideos(Pageable paging, Predicate<Video> filter) {
        List<Video> videos = videoRepository.findAllByOrderByIdAsc();
        if (filter != null) {
            videos = videos.stream().filter(filter).toList();
        }
        return new PageImpl<>(videoMapper.toVideoDtos(PageUtils.getContentPage(paging, videos)), paging, videos.size());
    }

    public VideoDto getVideoById(Long videoId) {
        Video video = videoRepository
                .findById(videoId)
                .orElseThrow(() -> new EntityNotFoundException("Video with id %d not found".formatted(videoId)));
        return videoMapper.toVideoDto(video);
    }

    public String deleteVideoById(Long videoId) {
        if (!videoRepository.existsById(videoId)) {
            throw new EntityNotFoundException("Video with id %d not found".formatted(videoId));
        }
        try {
            Video video = videoRepository.findById(videoId).orElseThrow();
            s3Service.deleteVideo(parseFilename(video.getUrl()));
            videoRepository.delete(video);
            return "successfully deleted video with id %d.".formatted(videoId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "error deleting video with id %d.".formatted(videoId);
    }

    public String parseFilename(String url) {
        Pattern regex = Pattern.compile("[^/]+$", Pattern.MULTILINE);
        Matcher matcher = regex.matcher(url);
        if (matcher.find()) {
            return matcher.group(0);
        }
        return url;
    }

    public void likeVideo(Long videoId) {
        Video video = videoRepository.findById(videoId)
                .orElseThrow(() -> new EntityNotFoundException("Video with id %d not found"
                        .formatted(videoId)));
        Long likes = video.getLikes();
        video.setLikes(likes + 1);
    }

    public void dislikeVideo(Long videoId) {
        Video video = videoRepository.findById(videoId)
                .orElseThrow(() -> new EntityNotFoundException("Video with id %d not found"
                        .formatted(videoId)));
        Long dislikes = video.getDislikes();
        video.setDislikes(dislikes + 1);
    }

    public void removeLikeVideo(long videoId) {
        Video video = videoRepository.findById(videoId)
                .orElseThrow(() -> new EntityNotFoundException("Video with id %d not found"
                        .formatted(videoId)));
        Long likes = video.getLikes();
        video.setLikes(likes - 1);
    }

    public void removeDislikeVideo(long videoId) {
        Video video = videoRepository.findById(videoId)
                .orElseThrow(() -> new EntityNotFoundException("Video with id %d not found"
                        .formatted(videoId)));
        Long dislikes = video.getDislikes();
        video.setDislikes(dislikes - 1);
    }

    public String uploadVideo(VideoDto videoDto, MultipartFile file) {
        try {
            String url = s3Service.uploadVideo(file);
            videoDto.setUrl(url);
            BufferedImage thumbnail = thumbnailTaker.getThumbnailFromVideo(url);
            String thumbnailUrl = s3Service.uploadThumbnail(thumbnail, file.getOriginalFilename() + "-thumbnail.jpg");
            videoDto.setThumbnail(thumbnailUrl);
            videoRepository.save(videoMapper.toVideo(videoDto));
        } catch (IOException e) {
            System.out.println("Error uploading video");
        } catch (FrameGrabber.Exception e) {
            throw new RuntimeException(e);
        }
        return "Video uploaded";
    }

    public Page<VideoDto> getTopVideosByLikes(Pageable paging) {
        List<VideoDto> videoDtos = videoMapper.toVideoDtos(videoRepository.findAll());
        videoDtos.sort(Comparator.comparingLong(VideoDto::getRating).reversed());
        return new PageImpl<>(PageUtils.getContentPage(paging, videoDtos), paging, videoDtos.size());
    }

    public Page<VideoDto> getFilteredVideos(String pattern, Pageable paging) {
        List<Video> allVideos = videoRepository.findAll();
        List<Video> filteredVideos = allVideos.stream().
                filter(video ->
                        video.getTags().stream()
                                .anyMatch(tag -> tag.toLowerCase().contains(pattern.toLowerCase())))
                .filter(video -> video.getVisibility().equals(VideoVisibility.PUBLIC))
                .toList();

        return new PageImpl<>(videoMapper.toVideoDtos(PageUtils.getContentPage(paging, filteredVideos)), paging, filteredVideos.size());

    }
}
