package com.project.ToDoList_App.service;

import java.util.List;
import java.util.Optional;

import com.project.ToDoList_App.entity.Status;
import com.project.ToDoList_App.entity.Task;

public interface ITaskService {

	public void addTask(Task task);
	public List<Task> fetchAllTask();
	public void delete(Integer id);
	
	public Optional<Task> getTaskById(Integer id);
	long countByStatus(Status status);
	long countOverdue();
	long countAll();
	List<Task> upcomingTasks();
	List<Task> searchTasks(Status status, String keyword);
	
}
