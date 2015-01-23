package com.proj.repository;

import com.proj.model.Project;
import com.proj.model.Task;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by F.U.C.K on 23-Jan-15.
 */
public interface ProjectRepository extends CrudRepository<Project,Integer> {

    @Query("select project from Project project where project.name like ?1")
    List<Project> findByName(String name);

    List<Project> findByAccountOwner(String accountOwner);

//    List<Task>
}

