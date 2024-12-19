package org.soulasphyxia.webcourseproject.entity.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.soulasphyxia.webcourseproject.entity.VideoVisibility;

@Getter
@Setter
@Data
public class UpdateVideoDto {
    private String title;
    private String content;
    private VideoVisibility visibility;
    private Long tagId;
    private String tagTitle;
}
