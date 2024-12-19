package org.soulasphyxia.webcourseproject.entity.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Getter
@Setter
public class UserActionsDateDto {
    private LocalDate date;
    private LocalTime from;
    private LocalTime to;
}
