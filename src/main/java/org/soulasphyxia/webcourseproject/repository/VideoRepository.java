package org.soulasphyxia.webcourseproject.repository;

import org.soulasphyxia.webcourseproject.entity.Video;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VideoRepository extends JpaRepository<Video, Long> {

    boolean existsById(Long id);

    @Query("SELECT v from Video v WHERE v.tag.id = :tagId and v.visibility = 'PUBLIC'")
    List<Video> findAllPublicByTagId(Long tagId, Pageable pageable);

    @Query("SELECT v from Video v where v.visibility = 'PUBLIC'")
    List<Video> findAllPublic();

    @Query("SELECT v from Video v where v.title like :title%")
    List<Video> findAllByTitle(@Param("title") String title);
}
