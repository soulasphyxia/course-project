package org.soulasphyxia.webcourseproject.repository;

import org.soulasphyxia.webcourseproject.entity.UserAction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface UserActionRepository extends JpaRepository<UserAction, Long>, JpaSpecificationExecutor<UserAction> {

}
