package com.project.ToDoList_App.configuration;

import java.security.Principal;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalModelAttributes {

    @ModelAttribute("username")
    public String username(Principal principal) {
        return principal != null ? principal.getName() : null;
    }
}