package com.project.service;

import com.project.model.Account;
import com.project.model.Project;
import com.project.model.Task;

public interface ProjectService {
    void addProject(Project project);
    void addTask(Task task);
    Account getAccount(String email);
    Project getProject(String id);
    Task getTask(Project project);

}
