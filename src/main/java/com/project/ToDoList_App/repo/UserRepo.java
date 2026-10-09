package com.project.ToDoList_App.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.ToDoList_App.entity.User;
@Repository
public interface UserRepo extends JpaRepository<User, String> {

	public User findByUsername(String username);

	public boolean existsByUsername(String trim);
}
