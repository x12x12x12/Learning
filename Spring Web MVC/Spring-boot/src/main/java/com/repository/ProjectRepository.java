package com.repository;

import com.model.Project;
import com.model.Task;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

//@RestResource(path = "project")
public interface ProjectRepository extends CrudRepository<Project,Integer> {

    @Query("select project from Project project where project.name like ?1")
    List<Project> findByName(String name);

    @Query("select task from Task  task where task.project.projectId =?1")
    List<Task> getListOfTaskById(Integer projectId);

//    @Query("select account from Account account,Project project where account.id=project.accountOwner.id and project.projectId=?1 ")
//    Account getAccountOwner(Integer projectId);

}

