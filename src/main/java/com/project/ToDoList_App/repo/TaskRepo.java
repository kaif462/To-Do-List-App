package com.project.ToDoList_App.repo;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.ToDoList_App.entity.Status;
import com.project.ToDoList_App.entity.Task;
import com.project.ToDoList_App.entity.User;

@Repository
public interface TaskRepo extends JpaRepository<Task, Integer> {

    List<Task> findByUser(User user, Sort sort);
    List<Task> findByUserAndStatus(User user, Status status, Sort sort);
    List<Task> findByUserAndTitleContainingIgnoreCase(User user, String keyword, Sort sort);
    List<Task> findByUserAndStatusAndTitleContainingIgnoreCase(User user, Status status, String keyword, Sort sort);

    Optional<Task> findByIdAndUser(Integer id, User user);

    long countByUser(User user);
    long countByUserAndStatus(User user, Status status);
    long countByUserAndDateBeforeAndStatusNot(User user, LocalDate date, Status status);

    List<Task> findTop5ByUserAndStatusNotOrderByDateAsc(User user, Status status);
}