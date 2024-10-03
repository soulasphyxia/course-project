package org.soulasphyxia.webcourseproject.mapper;

import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.soulasphyxia.webcourseproject.entity.Video;
import org.soulasphyxia.webcourseproject.entity.dto.VideoDto;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface VideoMapper {

    @Mapping(source = "tags", target = "tags", qualifiedByName = "tagsToEntity")
    @Mapping(source = "likes", target = "likes", defaultValue = "0L")
    @Mapping(source = "dislikes", target = "dislikes", defaultValue = "0L")
    Video toVideo(VideoDto videoDto);

    @Mapping(source = "tags", target = "tags", qualifiedByName = "tagsToDto")
    @Mapping(target = "rating", expression = "java(this.getRating(video))")
    VideoDto toVideoDto(Video video);

    List<VideoDto> toVideoDtos(List<Video> videos);

    @Named("tagsToEntity")
    default List<String> tagsToEntity(String tags) {
        List<String> tagsList = List.of(tags.split(", "));
        return tagsList.stream()
                .map(String::toLowerCase)
                .toList();
    }

    @Named("tagsToDto")
    default String tagsToDto(List<String> tags) {
        tags = tags.stream()
                .map(StringUtils::capitalize)
                .toList();
        return String.join(", ", tags);
    }

    @Named("getRating")
    default Long getRating(Video video) {
        return video.getLikes() - video.getDislikes();
    }
}
