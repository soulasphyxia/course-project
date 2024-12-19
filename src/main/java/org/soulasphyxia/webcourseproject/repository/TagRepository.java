package org.soulasphyxia.webcourseproject.repository;

import org.soulasphyxia.webcourseproject.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

    @Query(value = """
        SELECT t.id, t.title
        FROM tag t
        JOIN public.video v on t.id = v.tag_id
        GROUP BY t.id, t.title
        ORDER BY count(*) DESC
    """, nativeQuery = true)
    List<Tag> findTop10();
}
