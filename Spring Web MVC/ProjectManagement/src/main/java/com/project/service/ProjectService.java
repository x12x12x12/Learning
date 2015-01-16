package com.project.service;

import com.project.model.Account;
import com.project.model.Project;
import com.project.model.Task;

import java.util.List;

public interface ProjectService {
    void addProject(Project project);
    void updateProject(Project project);
    void addTask(Task task);
    void updateTask(Task task);
    Project getProject(String id);
    List<Project> getListProject(String accountOwner);
    List<Task> getListTask(String parent);
    Task getTask(Project project);

}
