package com.proj.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by F.U.C.K on 23-Jan-15.
 */
@Entity
@Table(name="project")
public class Project{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="project_id")
    Integer projectId;

    @Column(name = "name",nullable = false,length = 300)
    String name;

    @Column(name="owner",nullable = false,length = 100)
    String accountOwner;

    @OneToMany(mappedBy = "project",cascade = CascadeType.ALL,targetEntity = Task.class,fetch = FetchType.EAGER)
//    @JoinTable(name = "task_id")
    private List<Task> listOfTask;

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccountOwner() {
        return accountOwner;
    }

    public void setAccountOwner(String accountOwner) {
        this.accountOwner = accountOwner;
    }

    public List<Task> getListOfTask() {
        return listOfTask;
    }

    public void setListOfTask(List<Task> listOfTask) {
        this.listOfTask = listOfTask;
    }

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//
//        Project project = (Project) o;
//
//        if (accountOwner != null ? !accountOwner.equals(project.accountOwner) : project.accountOwner != null)
//            return false;
//        if (listOfTask != null ? !listOfTask.equals(project.listOfTask) : project.listOfTask != null) return false;
//        if (!name.equals(project.name)) return false;
//        if (!projectId.equals(project.projectId)) return false;
//
//        return true;
//    }
//
//    @Override
//    public int hashCode() {
//        int result = projectId.hashCode();
//        result = 31 * result + name.hashCode();
//        result = 31 * result + (accountOwner != null ? accountOwner.hashCode() : 0);
//        result = 31 * result + (listOfTask != null ? listOfTask.hashCode() : 0);
//        return result;
//    }
}
