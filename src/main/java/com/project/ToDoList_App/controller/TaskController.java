package com.project.ToDoList_App.controller;

import java.security.Principal;
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

    private final ITaskService service;

    public TaskController(ITaskService service) {
        this.service = service;
    }

    @ModelAttribute("options")
    public Status[] statusOptions() {
        return Status.values();
    }

    @GetMapping("/")
    public String home(Model model, Principal principal) {
        String user = principal.getName();
        model.addAttribute("totalCount", service.countAll(user));
        model.addAttribute("todoCount", service.countByStatus(user, Status.TODO));
        model.addAttribute("inProgressCount", service.countByStatus(user, Status.IN_PROGRESS));
        model.addAttribute("doneCount", service.countByStatus(user, Status.DONE));
        model.addAttribute("overdueCount", service.countOverdue(user));
        model.addAttribute("upcoming", service.upcomingTasks(user));
        return "home";
    }

    @GetMapping("/task-form")
    public String addTaskForm(Model model) {
        model.addAttribute("task", new Task());
        return "taskForm";
    }

    @PostMapping("/add-task")
    public String saveTask(@Valid @ModelAttribute("task") Task task,
                           BindingResult result,
                           Principal principal,
                           RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "taskForm";
        }

        boolean isNew = (task.getId() == null);
        if (!service.saveTask(task, principal.getName())) {
            redirectAttributes.addFlashAttribute("errorMessage", "Task not found.");
            return "redirect:/view-task";
        }

        redirectAttributes.addFlashAttribute("successMessage",
                isNew ? "Task added successfully!" : "Task updated successfully!");
        return "redirect:/view-task";
    }

    @GetMapping("/view-task")
    public String viewTask(@RequestParam(required = false) Status status,
                           @RequestParam(required = false) String keyword,
                           Model model, Principal principal) {
        model.addAttribute("tasks", service.searchTasks(principal.getName(), status, keyword));
        model.addAttribute("selectedStatus", status);
        model.addAttribute("keyword", keyword);
        return "view";
    }

    @GetMapping("/edit/{id}")
    public String editTask(@PathVariable Integer id, Model model,
                           Principal principal, RedirectAttributes redirectAttributes) {
        Optional<Task> task = service.getTaskById(id, principal.getName());
        if (task.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Task not found.");
            return "redirect:/view-task";
        }
        model.addAttribute("task", task.get());
        return "taskForm";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Integer id, Principal principal,
                         RedirectAttributes redirectAttributes) {
        if (service.delete(id, principal.getName())) {
            redirectAttributes.addFlashAttribute("successMessage", "Task deleted.");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Task not found.");
        }
        return "redirect:/view-task";
    }
}