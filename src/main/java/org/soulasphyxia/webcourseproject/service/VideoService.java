package org.soulasphyxia.webcourseproject.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.bytedeco.javacv.FrameGrabber;
import org.soulasphyxia.webcourseproject.entity.Video;
import org.soulasphyxia.webcourseproject.entity.VideoVisibility;
import org.soulasphyxia.webcourseproject.entity.dto.RatingDto;
import org.soulasphyxia.webcourseproject.entity.dto.UpdateVideoDto;
import org.soulasphyxia.webcourseproject.entity.dto.VideoDto;
import org.soulasphyxia.webcourseproject.exception.ForbiddenAccessException;
import org.soulasphyxia.webcourseproject.mapper.VideoMapper;
import org.soulasphyxia.webcourseproject.repository.VideoRepository;
import org.soulasphyxia.webcourseproject.utils.AuthenticationSystem;
import org.soulasphyxia.webcourseproject.utils.PageUtils;
import org.soulasphyxia.webcourseproject.utils.ThumbnailTaker;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
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
    private final TagService tagService;

    public Page<VideoDto> getVideos(Pageable paging) {
        List<Video> videos = videoRepository.findAll();
        return new PageImpl<>(videoMapper.toVideoDtos(PageUtils.getContentPage(paging, videos)), paging, videos.size());
    }

    public Page<VideoDto> getPublicVideos(Pageable paging) {
        List<Video> videos = videoRepository.findAllPublic();
        return new PageImpl<>(videoMapper.toVideoDtos(PageUtils.getContentPage(paging, videos)), paging, videos.size());
    }

    public Page<VideoDto> getPublicVideosByTagId(Long tagId, Pageable paging) {
        List<Video> videos = videoRepository.findAllPublicByTagId(tagId, paging);
        return new PageImpl<>(videoMapper.toVideoDtos(PageUtils.getContentPage(paging, videos)), paging, videos.size());
    }

    public Page<VideoDto> getVideosByTitle(String title, Pageable paging) {
        List<Video> videos = videoRepository.findAllByTitle(title);
        return new PageImpl<>(videoMapper.toVideoDtos(PageUtils.getContentPage(paging, videos)), paging, videos.size());
    }

    public VideoDto getVideoById(Long videoId) {
        Video video = videoRepository
                .findById(videoId)
                .orElseThrow(() -> new EntityNotFoundException("Video with id %d not found".formatted(videoId)));
        if (video.getVisibility().equals(VideoVisibility.PRIVATE) && !AuthenticationSystem.isLogged()) {
            throw new ForbiddenAccessException();
        }
        return videoMapper.toVideoDto(video);
    }

    public void editVideo(Long videoId, UpdateVideoDto updateVideoDto) {
        Video targetVideo = videoRepository.findById(videoId).orElseThrow(() ->
            new EntityNotFoundException("Video with id %d not found".formatted(videoId))
        );
        if (updateVideoDto.getTitle() != null) {
            targetVideo.setTitle(updateVideoDto.getTitle());
        }
        if (updateVideoDto.getContent() != null) {
            targetVideo.setContent(updateVideoDto.getContent());
        }
        if (updateVideoDto.getTagId() != null) {
            targetVideo.setTag(tagService.findById(updateVideoDto.getTagId()));
        }
        if (updateVideoDto.getVisibility() != null) {
            targetVideo.setVisibility(updateVideoDto.getVisibility());
        }
        videoRepository.save(targetVideo);
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

    private String parseFilename(String url) {
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
        if (likes != 0) {
            video.setLikes(likes - 1);
        }
    }

    public void removeDislikeVideo(long videoId) {
        Video video = videoRepository.findById(videoId)
                .orElseThrow(() -> new EntityNotFoundException("Video with id %d not found"
                        .formatted(videoId)));
        Long dislikes = video.getDislikes();
        if (dislikes != 0) {
            video.setDislikes(dislikes - 1);
        }
    }

    public String uploadVideo(VideoDto videoDto, MultipartFile file) {
        try {
            String url = s3Service.uploadVideo(file);
            videoDto.setUrl(url);
            BufferedImage thumbnail = thumbnailTaker.getThumbnailFromVideo(url);
            String thumbnailUrl = s3Service.uploadThumbnail(thumbnail, file.getOriginalFilename() + "-thumbnail.jpg");
            videoDto.setThumbnail(thumbnailUrl);

            Video video = videoMapper.toVideo(videoDto);
            video.setTag(tagService.findById(videoDto.getTagId()));
            videoRepository.save(video);
        } catch (IOException | FrameGrabber.Exception e) {
            throw new RuntimeException(e);
        }
        return "Video uploaded";
    }

    public Page<VideoDto> getTopVideosByLikes(Pageable paging) {
        List<VideoDto> videoDtos = videoMapper.toVideoDtos(videoRepository.findAll());
        videoDtos.sort(Comparator.comparingLong(VideoDto::getRating).reversed());
        return new PageImpl<>(PageUtils.getContentPage(paging, videoDtos), paging, videoDtos.size());
    }

    @Transactional
    public void changeRating(long videoId, RatingDto ratingDto) {
        Video video = videoRepository.findById(videoId)
                .orElseThrow(() -> new EntityNotFoundException("Video with id %d not found"
                        .formatted(videoId)));
        video.setLikes(ratingDto.likes());
        video.setDislikes(ratingDto.dislikes());
    }
}
