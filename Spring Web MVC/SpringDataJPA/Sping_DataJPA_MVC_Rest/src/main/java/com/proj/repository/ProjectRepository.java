package com.proj.repository;

import com.proj.model.Project;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by fuck on 22/01/15.
 */
public interface ProjectRepository extends CrudRepository<Project,Integer> {

    @Query(value = "SELECT * FROM Project WHERE name = ?0", nativeQuery = true)
    public Project findByName(String name);

//    @Query("select data from Project where data.accountOwner=?1")
//    public Project findByAccountOwner(String accoutnOwner);
}
