package com.project.ToDoList_App.controller;

import java.util.Objects;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.project.ToDoList_App.entity.User;
import com.project.ToDoList_App.repo.UserRepo;

import jakarta.validation.Valid;

@Controller
public class AuthController {
	private UserRepo userRepo;
	
	public AuthController(UserRepo userRepo) {
		super();
		this.userRepo = userRepo;
	}
	@GetMapping("/register")
	public String registerForm(Model model) {
		model.addAttribute("user", new User());
		return "register";
	}
	@PostMapping("/register")
	public String  register(@Valid @ModelAttribute User user,BindingResult result,RedirectAttributes redirectAttributes) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
		 if (user.getUsername() != null && userRepo.existsByUsername(user.getUsername().trim())) {
	            result.rejectValue("username", "exists", "Username already taken");
	        }
		 if (!Objects.equals(user.getPassword(), user.getConfirmPassword())) {
			    result.rejectValue(
			        "confirmPassword",
			        "mismatch",
			        "Passwords do not match"
			    );
			}
		 if (result.hasErrors()) {
	            return "register";
	        }
		 User newUser=new User();
		 user.setUsername(user.getUsername().trim());
	        user.setPassword(encoder.encode(user.getPassword()));
	        userRepo.save(user);
	        redirectAttributes.addFlashAttribute("successMessage", "Account created. Please log in.");
	        return "redirect:/login";
	}

    @GetMapping("/login")
    public String login() {
        return "login";
    }

}
