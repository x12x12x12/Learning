package com.project.service;


import com.mongodb.MongoException;
import com.project.model.Account;
import com.project.model.Project;
import com.project.model.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.Date;
import java.util.List;

public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private MongoTemplate mongoTemplate;
    private Query query;

    public ProjectServiceImpl(MongoTemplate mongoTemplate) {this.mongoTemplate=mongoTemplate; }

    @Override
    public void addProject(Project project) throws MongoException {
        if(!mongoTemplate.collectionExists(Project.class)){
            mongoTemplate.createCollection(Project.class);
        }
        String id=project.getAccountOwner()+project.getName()+new Date().toString();
        project.setId("PROJ-"+Integer.toString(Math.abs(id.hashCode())));
        mongoTemplate.insert(project);
    }

    @Override
    public void addTask(Task task) throws MongoException {
        if(!mongoTemplate.collectionExists(Task.class)){
            mongoTemplate.createCollection(Task.class);
        }
        String id_task=task.getName()+task.getParent()+task.getRootProject()+new Date().toString();
        task.setId("TASK-"+Integer.toString(Math.abs(id_task.hashCode())));
        mongoTemplate.insert(task);
    }

    @Override
    public Project getProject(String id) {
        query=new Query(Criteria.where("id").is(id));
        return mongoTemplate.findOne(query,Project.class);
    }

    @Override
    public List<Project> getListProject(String accountOwner) throws MongoException {
        query=new Query(Criteria.where("accountOwner").is(accountOwner));
        return mongoTemplate.find(query,Project.class);
    }

    @Override
    public List<Task> getListTask(String parent) {   // 1st param is rootProject
        query=new Query(Criteria.where("parent").is(parent));
        List<Task> list=mongoTemplate.find(query,Task.class);
        for(Task task : list){
            task.setTaskChild(getListTask(task.getId()));
        }
        return list;
    }

    @Override
    public Task getTask(Project project) {
        return null;
    }
}
