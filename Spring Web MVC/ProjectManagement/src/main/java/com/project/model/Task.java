package com.project.model;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document(collection="task")
public class Task {
    private String id;
    private String name;
    private String rootProject;
    private String parent;
    private List<Task> taskChild;
    private Date createDate;
    private Date startDate;
    private Date dueDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRootProject() {
        return rootProject;
    }

    public void setRootProject(String rootProject) {
        this.rootProject = rootProject;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public List<Task> getTaskChild() {
        return taskChild;
    }

    public void setTaskChild(List<Task> taskChild) {
        this.taskChild = taskChild;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }
}
