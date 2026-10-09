package com.project.ToDoList_App.entity;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Task {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
  private Integer id;
	@NotBlank(message = "Title is required")
  private String title;
  private String description;
  private LocalDate date;
  @Enumerated(EnumType.STRING)
  private Status status=Status.TODO;
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;
  public Integer getId() {
	return id;
  }
  public void setId(Integer id) {
	this.id = id;
  }
  
  public String getTitle() {
	return title;
  }
  public void setTitle(String title) {
	this.title = title;
  }
  public String getDescription() {
	return description;
  }
  public void setDescription(String description) {
	this.description = description;
  }
  public LocalDate getDate() {
	return date;
  }
  public void setDate(LocalDate date) {
	this.date = date;
  }
  public Status getStatus() {
	return status;
  }
  public void setStatus(Status status) {
	this.status = status;
  }
  public boolean isOverdue() {
	    return date != null
	        && status != Status.DONE
	        && date.isBefore(java.time.LocalDate.now());
	}
  public User getUser() {
	return user;
  }
  public void setUser(User user) {
	this.user = user;
  }
  
}
