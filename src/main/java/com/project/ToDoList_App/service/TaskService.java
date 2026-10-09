package com.project.ToDoList_App.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.project.ToDoList_App.entity.Status;
import com.project.ToDoList_App.entity.Task;
import com.project.ToDoList_App.entity.User;
import com.project.ToDoList_App.repo.TaskRepo;
import com.project.ToDoList_App.repo.UserRepo;

@Service
public class TaskService implements ITaskService {

    private final TaskRepo repo;
    private final UserRepo userRepo;

    public TaskService(TaskRepo repo, UserRepo userRepo) {
        this.repo = repo;
        this.userRepo = userRepo;
    }

    private User getUser(String username) {
        return userRepo.findByUsername(username);
    }

    @Override
    public boolean saveTask(Task task, String username) {
        User user = getUser(username);
        // Editing: make sure the task really belongs to this user
        if (task.getId() != null && repo.findByIdAndUser(task.getId(), user).isEmpty()) {
            return false;
        }
        task.setUser(user);
        repo.save(task);
        return true;
    }

    @Override
    public Optional<Task> getTaskById(Integer id, String username) {
        return repo.findByIdAndUser(id, getUser(username));
    }

    @Override
    public boolean delete(Integer id, String username) {
        Optional<Task> task = repo.findByIdAndUser(id, getUser(username));
        task.ifPresent(repo::delete);
        return task.isPresent();
    }

    @Override
    public List<Task> searchTasks(String username, Status status, String keyword) {
        User user = getUser(username);
        Sort sort = Sort.by("date").ascending();
        boolean hasKeyword = keyword != null && !keyword.isBlank();

        if (status != null && hasKeyword) {
            return repo.findByUserAndStatusAndTitleContainingIgnoreCase(user, status, keyword.trim(), sort);
        } else if (status != null) {
            return repo.findByUserAndStatus(user, status, sort);
        } else if (hasKeyword) {
            return repo.findByUserAndTitleContainingIgnoreCase(user, keyword.trim(), sort);
        }
        return repo.findByUser(user, sort);
    }

    @Override
    public long countAll(String username) {
        return repo.countByUser(getUser(username));
    }

    @Override
    public long countByStatus(String username, Status status) {
        return repo.countByUserAndStatus(getUser(username), status);
    }

    @Override
    public long countOverdue(String username) {
        return repo.countByUserAndDateBeforeAndStatusNot(getUser(username), LocalDate.now(), Status.DONE);
    }

    @Override
    public List<Task> upcomingTasks(String username) {
        return repo.findTop5ByUserAndStatusNotOrderByDateAsc(getUser(username), Status.DONE);
    }
}