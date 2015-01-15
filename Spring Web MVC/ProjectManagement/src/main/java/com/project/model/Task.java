package com.project.model;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection="task")
public class Task {
    private String id;
    private String name;
    private String rootProject;
    private String parent;
    private List<Task> taskChild;

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
}
