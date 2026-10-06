package com.project.ToDoList_App.repo;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.ToDoList_App.entity.Status;
import com.project.ToDoList_App.entity.Task;
@Repository
public interface TaskRepo extends JpaRepository<Task, Integer> {


	long countByStatus(Status status);

	long countByDateBeforeAndStatusNot(LocalDate date, Status status);

	List<Task> findTop5ByStatusNotOrderByDateAsc(Status status);
	

	    List<Task> findByStatus(Status status, Sort sort);

	    List<Task> findByTitleContainingIgnoreCase(String keyword, Sort sort);

	    List<Task> findByStatusAndTitleContainingIgnoreCase(Status status, String keyword, Sort sort);
	
}
