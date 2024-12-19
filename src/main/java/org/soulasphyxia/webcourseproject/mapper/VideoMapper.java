package org.soulasphyxia.webcourseproject.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.soulasphyxia.webcourseproject.entity.Video;
import org.soulasphyxia.webcourseproject.entity.dto.VideoDto;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface VideoMapper {

    @Mapping(source = "likes", target = "likes", defaultValue = "0L")
    @Mapping(source = "dislikes", target = "dislikes", defaultValue = "0L")
    Video toVideo(VideoDto videoDto);

    @Mapping(target = "rating", expression = "java(this.getRating(video))")
    @Mapping(source = "tag.id", target = "tagId")
    @Mapping(source = "tag.title", target = "tagTitle")
    VideoDto toVideoDto(Video video);

    List<VideoDto> toVideoDtos(List<Video> videos);

    @Named("getRating")
    default Long getRating(Video video) {
        return video.getLikes() - video.getDislikes();
    }
}
