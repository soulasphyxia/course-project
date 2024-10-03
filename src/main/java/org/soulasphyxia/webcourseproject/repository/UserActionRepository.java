package org.soulasphyxia.webcourseproject.repository;

import org.soulasphyxia.webcourseproject.entity.UserAction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface UserActionRepository extends JpaRepository<UserAction, Long> {

    Page<UserAction> findUserActionsByDate(LocalDate date, Pageable pageable);

    Page<UserAction> findUserActionsByDateAndTimeAfter(LocalDate date, LocalTime from, Pageable pageable);

    Page<UserAction> findUserActionsByDateAndTimeBefore(LocalDate date, LocalTime to, Pageable pageable);

    Page<UserAction> findUserActionsByDateAndTimeBetween(LocalDate date, LocalTime from, LocalTime to, Pageable pageable);

}
