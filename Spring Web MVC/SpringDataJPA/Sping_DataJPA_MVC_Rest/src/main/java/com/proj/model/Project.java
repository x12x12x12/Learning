package com.proj.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Created by fuck on 22/01/15.
 */
@Entity
//@NamedQuery(name="Project.findByName",query = "select project from Project where ")
@Table(name="Project")
public class Project{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id")
    Integer projectId;

    @Column(name = "name",nullable = false,length = 300)
    String name;

    @Column(name="owner",nullable = false,length = 100)
    String accountOwner;

    @OneToMany(mappedBy = "project",targetEntity = Task.class)
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
}
