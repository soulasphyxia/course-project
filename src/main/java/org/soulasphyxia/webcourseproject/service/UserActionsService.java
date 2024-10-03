package org.soulasphyxia.webcourseproject.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.soulasphyxia.webcourseproject.entity.UserAction;
import org.soulasphyxia.webcourseproject.user_actions.writer.UserActionsWriter;
import org.soulasphyxia.webcourseproject.repository.UserActionRepository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserActionsService {
    private final UserActionRepository actionRepository;
    private final S3Service s3Service;
    private final List<UserActionsWriter> writers;

    public Page<UserAction> getUserActions(String date, String from, String to, Pageable paging) {
        if (!date.isEmpty() && from.isEmpty() && to.isEmpty()) {
            return actionRepository.findUserActionsByDate(LocalDate.parse(date), paging);
        }
        if (!date.isEmpty() && !from.isEmpty() && to.isEmpty()) {
            return actionRepository.findUserActionsByDateAndTimeAfter(LocalDate.parse(date), LocalTime.parse(from), paging);
        }
        if (!date.isEmpty() && from.isEmpty()) {
            return actionRepository.findUserActionsByDateAndTimeBefore(LocalDate.parse(date), LocalTime.parse(to), paging);
        }
        if (!date.isEmpty()) {
            return actionRepository.findUserActionsByDateAndTimeBetween(LocalDate.parse(date), LocalTime.parse(from), LocalTime.parse(to), paging);
        }
        return actionRepository.findAll(paging);
    }

    @Scheduled(cron = "${user-actions.cron}")
    public void saveLog() {
        for (UserActionsWriter writer : writers) {
            try(InputStream bytes = writer.write(actionRepository.findAll())) {
                s3Service.uploadLog(bytes, writer.getLogType());
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
            log.info("SAVED LOG .{}", writer.getLogType());
        }
    }

    public void add(UserAction action) {
        actionRepository.save(action);
    }
}
