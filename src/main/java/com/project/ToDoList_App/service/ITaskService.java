package com.project.ToDoList_App.service;

import java.util.List;
import java.util.Optional;

import com.project.ToDoList_App.entity.Status;
import com.project.ToDoList_App.entity.Task;

public interface ITaskService {
    boolean saveTask(Task task, String username);
    Optional<Task> getTaskById(Integer id, String username);
    boolean delete(Integer id, String username);
    List<Task> searchTasks(String username, Status status, String keyword);

    long countAll(String username);
    long countByStatus(String username, Status status);
    long countOverdue(String username);
    List<Task> upcomingTasks(String username);
}