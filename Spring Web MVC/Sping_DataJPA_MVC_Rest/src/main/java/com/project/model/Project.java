package com.project.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="project")
public class Project{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="project_id")
    private Integer projectId;

    @Column(name = "name",nullable = false,length = 300)
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id",referencedColumnName = "account_id")
    private Account accountOwner;

    @OneToMany(mappedBy = "project",cascade = CascadeType.ALL,targetEntity = Task.class,fetch = FetchType.LAZY)
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

    public Account getAccountOwner() {
        return accountOwner;
    }

    public void setAccountOwner(Account accountOwner) {
        this.accountOwner = accountOwner;
    }

    public List<Task> getListOfTask() {
        return listOfTask;
    }

    public void setListOfTask(List<Task> listOfTask) {
        this.listOfTask = listOfTask;
    }

}
