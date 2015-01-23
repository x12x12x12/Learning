package com.proj.model;


import javax.persistence.*;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.List;

/**
 * Created by F.U.C.K on 23-Jan-15.
 */
@Entity
@Table(name = "task")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "task_id")
    private Integer taskId;

    @Column(name = "name",nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "project_id",referencedColumnName = "project_id")
    private Project project;

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public void setProject(Project project) {
        this.project = project;
    }

    public Project getProject() {
        return project;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Task task = (Task) o;

        if (!name.equals(task.name)) return false;
        if (project != null ? !project.equals(task.project) : task.project != null) return false;
        if (!taskId.equals(task.taskId)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = taskId.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + (project != null ? project.hashCode() : 0);
        return result;
    }
}
