package org.soulasphyxia.webcourseproject.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.soulasphyxia.webcourseproject.entity.Tag;
import org.soulasphyxia.webcourseproject.repository.TagRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagService {
    private final TagRepository tagRepository;

    public List<Tag> findAll() {
        return tagRepository.findAll(Sort.by(Sort.Direction.ASC, "title"));
    }

    public List<Tag> findTop10() {
        return tagRepository.findTop10();
    }

    public Tag findById(Long id) {
        return tagRepository.findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException("тема с id %d не найдена".formatted(id))
                );
    }

}
