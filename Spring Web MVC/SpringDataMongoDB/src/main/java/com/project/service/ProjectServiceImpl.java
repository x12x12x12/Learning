package com.project.service;


import com.mongodb.MongoException;
import com.project.model.Account;
import com.project.model.Project;
import com.project.model.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
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
        project.setCreateDate(new Date());
        mongoTemplate.insert(project);
    }

    @Override
    public void updateProject(Project project) throws MongoException {
        query = new Query();
        query.addCriteria(Criteria.where("_id").is(project.getId()));
        Update update = new Update();
        update.set("name",project.getName());
        update.set("startDate",project.getStartDate());
        update.set("dueDate",project.getDueDate());
        mongoTemplate.updateFirst(query,update,Project.class);
    }

    @Override
    public void addTask(Task task) throws MongoException {
        if(!mongoTemplate.collectionExists(Task.class)){
            mongoTemplate.createCollection(Task.class);
        }
        String id_task=task.getName()+task.getParent()+task.getRootProject()+new Date().toString();
        task.setId("TASK-"+Integer.toString(Math.abs(id_task.hashCode())));
        task.setCreateDate(new Date());
        mongoTemplate.insert(task);
    }

    @Override
    public void updateTask(Task task)throws MongoException {
        query = new Query();
        query.addCriteria(Criteria.where("_id").is(task.getId()));
        Update update = new Update();
        update.set("name",task.getName());
        update.set("startDate",task.getStartDate());
        update.set("dueDate",task.getDueDate());
        mongoTemplate.updateFirst(query,update,Task.class);
    }

    @Override
    public Project getProject(String id) throws MongoException {
        query=new Query(Criteria.where("id").is(id));
        return mongoTemplate.findOne(query,Project.class);
//          return projectRepo.findOne(Integer.parseInt(id));
    }

    @Override
    public List<Project> getListProject(String accountOwner) throws MongoException {
        query=new Query(Criteria.where("accountOwner").is(accountOwner));
        return mongoTemplate.find(query,Project.class);
    }

    @Override
    public List<Task> getListTask(String parent) throws MongoException {   // 1st param is rootProject
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
