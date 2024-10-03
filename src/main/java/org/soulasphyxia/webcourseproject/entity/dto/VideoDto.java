package org.soulasphyxia.webcourseproject.entity.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.soulasphyxia.webcourseproject.entity.VideoVisibility;

@Data
@Getter
@Setter
public class VideoDto {
    private Long id;
    private String title;
    private String content;
    private String url;
    private String thumbnail;
    private VideoVisibility visibility;
    private Long rating;
    private Long likes;
    private Long dislikes;
    private String tags;
}
