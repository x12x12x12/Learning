package com.project.service;


import com.mongodb.MongoException;
import com.project.model.Account;
import com.project.model.Project;
import com.project.model.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

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
        mongoTemplate.insert(project);
    }

    @Override
    public void addTask(Task task) throws MongoException {
        if(!mongoTemplate.collectionExists(Task.class)){
            mongoTemplate.createCollection(Task.class);
        }
        mongoTemplate.insert(task);
    }

    @Override
    public Project getProject(String id) {
        return null;
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
            task.setTaskChild(getListTask(task.getName()));
        }
        return list;
    }

    @Override
    public Task getTask(Project project) {
        return null;
    }
}
