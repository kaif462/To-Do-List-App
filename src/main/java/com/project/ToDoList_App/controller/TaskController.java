package com.project.ToDoList_App.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.project.ToDoList_App.entity.Status;
import com.project.ToDoList_App.entity.Task;
import com.project.ToDoList_App.service.ITaskService;

import jakarta.validation.Valid;

@Controller
public class TaskController {
	private ITaskService service;

	public TaskController(ITaskService service) {
		super();
		this.service = service;
	}
	@ModelAttribute("options")
	public Status[] statusOptions() {
	    return Status.values();
	}
     	
	@GetMapping("/task-form")
	public String addTaskForm(@ModelAttribute Task task,Model model) {
		model.addAttribute("task",task);
		return "taskForm";
	}
	@PostMapping("add-task")
	public String addTask( @Valid @ModelAttribute("task") Task task
			,BindingResult result,RedirectAttributes redirectAttributes) {
		if(result.hasErrors()) {
			return "taskForm";
		}
		service.addTask(task);
		redirectAttributes.addFlashAttribute("successMessage", "Task added successfully!");
		return "redirect:/view-task";
	}
	@GetMapping("/view-task")
	public String viewTask(@RequestParam(required = false) Status status,
	                       @RequestParam(required = false) String keyword,
	                       Model model) {
	    model.addAttribute("tasks", service.searchTasks(status, keyword));
	    model.addAttribute("selectedStatus", status);   // keeps dropdown selected
	    model.addAttribute("keyword", keyword);         // keeps search text
	    return "view";
	}
	@PostMapping("/delete/{id}")
	public String delete(@PathVariable Integer id ,RedirectAttributes redirectAttributes) {
		service.delete(id);
		redirectAttributes.addFlashAttribute("successMessage","Task deleted successfully");
		return "redirect:/view-task";
	}
	
	@GetMapping("/edit/{id}")
	public String editTask(@PathVariable Integer id,
			Model model,
			RedirectAttributes redirectAttributes) {

		Optional<Task> task = service.getTaskById(id);
		if (task.isEmpty()) {
			redirectAttributes.addFlashAttribute("errorMessage", "Task not found.");
			return "redirect:/view-task";
		}
		model.addAttribute("task", task.get());
		return "taskForm";
	}
	@GetMapping("/")
	public String home(Model model) {
	    model.addAttribute("totalCount", service.countAll());
	    model.addAttribute("todoCount", service.countByStatus(Status.TODO));
	    model.addAttribute("inProgressCount", service.countByStatus(Status.IN_PROGRESS));
	    model.addAttribute("doneCount", service.countByStatus(Status.DONE));
	    model.addAttribute("overdueCount", service.countOverdue());
	    model.addAttribute("upcoming", service.upcomingTasks());
	    return "home";
	}

}
