package org.soulasphyxia.webcourseproject.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.soulasphyxia.webcourseproject.entity.UserAction;
import org.soulasphyxia.webcourseproject.entity.dto.UserActionsDateDto;
import org.soulasphyxia.webcourseproject.filter.Filter;
import org.soulasphyxia.webcourseproject.user_actions.LogType;
import org.soulasphyxia.webcourseproject.user_actions.writer.UserActionsWriter;
import org.soulasphyxia.webcourseproject.repository.UserActionRepository;

import org.soulasphyxia.webcourseproject.utils.ResourceObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserActionsService {
    private final UserActionRepository actionRepository;
    private final List<UserActionsWriter> writers;
    private final List<Filter<UserActionsDateDto, UserAction>> dateFilters;

    public Page<UserAction> getUserActions(UserActionsDateDto dateDto, Pageable paging) {
        return actionRepository.findAll(getSpecification(dateDto), paging);
    }

    public void add(UserAction action) {
        actionRepository.save(action);
    }


    public ResourceObject download(UserActionsDateDto dateDto, LogType format) {
        UserActionsWriter writer = writers.stream()
                .filter(x -> x.getLogType().equals(format))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Конвертера логов с таким типом нет"));

        return new ResourceObject(
                getResourceTitle(dateDto),
                writer.write(actionRepository.findAll(getSpecification(dateDto))),
                MediaType.valueOf(format.mediaType)
                );
    }

    private String getResourceTitle(UserActionsDateDto dateDto) {
        if (dateDto.getDate() == null) {
            return "log-all-time";
        }
        return "log-%s".formatted(dateDto.getDate());
    }

    private Specification<UserAction> getSpecification(UserActionsDateDto dateDto) {
        return Specification.allOf(dateFilters.stream()
                .filter(filter -> filter.isApplicable(dateDto))
                .map(filter -> filter.apply(dateDto))
                .toList()
        );
    }
}
