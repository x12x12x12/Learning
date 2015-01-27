package com.project.repository;

import com.project.model.Task;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

//@RestResource(exported = false)
public interface TaskRepository extends CrudRepository<Task,Integer> {

    @Query("select task from Task task where task.taskParent.id =?1")
    List<Task> getListOfTaskChildById(Integer taskId);
}