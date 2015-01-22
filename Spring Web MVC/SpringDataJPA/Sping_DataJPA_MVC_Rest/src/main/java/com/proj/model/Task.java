package com.proj.model;


import javax.persistence.*;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.List;

/**
 * Created by F.U.C.K on 22-Jan-15.
 */
@Entity
@Table(name = "Task")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name",nullable = false)
    private String name;

//    @ManyToOne
//    @JoinColumn(name="task_id",referencedColumnName = "id")
//    private Task parent;

    @ManyToOne
    @JoinColumn(name = "project_id",referencedColumnName = "id")
    private Project project;

//    @OneToMany(mappedBy = "task",targetEntity = Task.class)
//    private List<Task> listOfTask;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

//    public Task getParent() {
//        return parent;
//    }
//
//    public void setParent(Task parent) {
//        this.parent = parent;
//    }

    public Project getProject() {
        return project;
    }

    public void setProjectRoot(Project projectRoot) {
        this.project = project;
    }

//    public List<Task> getListOfTask() {
//        return listOfTask;
//    }
//
//    public void setListOfTask(List<Task> listOfTask) {
//        this.listOfTask = listOfTask;
//    }
}
