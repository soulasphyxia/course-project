package org.soulasphyxia.webcourseproject.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.opencsv.bean.CsvBindAndJoinByPosition;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvCustomBindByName;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_actions")
public class UserAction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @CsvBindByName(column = "id", required = true)
    private Long id;

    @Column(name = "action")
    @CsvBindByName(column = "Действие", required = true)
    private String action;

    @Column(name = "datetime")
    @JsonProperty("datetime")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @CsvBindByName(column = "Дата", required = true)
    private LocalDateTime dateTime;

    @Column(name = "user_ip")
    @CsvBindByName(column = "IP пользователя", required = true)
    private String userIp;
}
