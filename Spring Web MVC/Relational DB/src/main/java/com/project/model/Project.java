package com.project.model;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Project")
public class Project {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private String id;
    private String name;
    private String accountOwner;
    private List<Task> taskList;
    private Date createDate;
    private Date startDate;
    private Date dueDate;

    public Project() {
    }

    public Project(String id, String name, String accountOwner, List<Task> taskList, Date createDate, Date startDate, Date dueDate) {
        this.id = id;
        this.name = name;
        this.accountOwner = accountOwner;
        this.taskList = taskList;
        this.createDate = createDate;
        this.startDate = startDate;
        this.dueDate = dueDate;
    }

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

    public String getAccountOwner() {
        return accountOwner;
    }

    public void setAccountOwner(String accountOwner) {
        this.accountOwner = accountOwner;
    }

    public List<Task> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<Task> taskList) {
        this.taskList = taskList;
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
