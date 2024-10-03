package org.soulasphyxia.webcourseproject.repository;

import org.soulasphyxia.webcourseproject.entity.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VideoRepository extends JpaRepository<Video, Long> {
    boolean existsById(Long id);
    List<Video> findAllByOrderByIdAsc();
}
