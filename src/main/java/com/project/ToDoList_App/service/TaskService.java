package com.project.ToDoList_App.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.project.ToDoList_App.entity.Status;
import com.project.ToDoList_App.entity.Task;
import com.project.ToDoList_App.repo.TaskRepo;
@Service
public class TaskService implements ITaskService  {
	private TaskRepo repo;
	

	public TaskService(TaskRepo repo) {
		super();
		this.repo = repo;
	}

	@Override
	public void addTask(Task task) {
		repo.save(task);
		
	}

	@Override
	public List<Task> fetchAllTask() {
		List<Task> tasks = repo.findAll();
		return tasks;
	}

	@Override
	public void delete(Integer id) {
		Optional<Task> task=repo.findById(id);
		if(task.get() != null) {
			repo.deleteById(id);
		}
		
	}

	
	@Override
	public Optional<Task> getTaskById(Integer id) {
		return repo.findById(id);
		
	}
	@Override
	public long countByStatus(Status status) {
	    return repo.countByStatus(status);
	}

	@Override
	public long countOverdue() {
	    return repo.countByDateBeforeAndStatusNot(LocalDate.now(), Status.DONE);
	}

	@Override
	public long countAll() {
	    return repo.count();
	}

	@Override
	public List<Task> upcomingTasks() {
	    return repo.findTop5ByStatusNotOrderByDateAsc(Status.DONE);
	}
	
	@Override
	public List<Task> searchTasks(Status status, String keyword) {
	    Sort sort = Sort.by("date").ascending();          // earliest date first
	    boolean hasKeyword = keyword != null && !keyword.isBlank();

	    if (status != null && hasKeyword) {
	        return repo.findByStatusAndTitleContainingIgnoreCase(status, keyword.trim(), sort);
	    } else if (status != null) {
	        return repo.findByStatus(status, sort);
	    } else if (hasKeyword) {
	        return repo.findByTitleContainingIgnoreCase(keyword.trim(), sort);
	    }
	    return repo.findAll(sort);
	}
}
